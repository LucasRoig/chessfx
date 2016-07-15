package chessfx.ui;

import java.util.Optional;

import javax.swing.event.ChangeEvent;

import chessfx.core.ChessColors;
import chessfx.core.game.IGame;
import chessfx.core.game.IOpening;
import chessfx.core.game.OpeningFactory;
import chessfx.data.Database;
import chessfx.ui.model.GameSelectionModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * Panneau de gauche avec l'arbre présentant les différentes ouvertures.
 * @author Lucas
 *
 */
public class DatabaseView extends AnchorPane {
	//Ouvertures factices servant simplement à créer les premiers noeuds de l'arbre
	private IOpening white;  
	private IOpening black;
	private IOpening root;
			
	private Database data;
	private GameSelectionModel model;
	private TreeView<IOpening> tree;
	private ContextMenu menuCreationWhite;
	private ContextMenu menuCreationBlack;
	private ContextMenu menuOpening;

	public DatabaseView() {
		//=================ouvertures factices===================
		OpeningFactory factory = new OpeningFactory();
		this.white = factory.getOpening();
		this.white.setName("White");
		this.black = factory.getOpening();
		this.black.setName("Black");
		this.root = factory.getOpening();
		this.root.setName("Root");
		
		this.tree = new TreeView<IOpening>();
		this.menuCreationWhite = new ContextMenu();

		TextInputDialog inputOpeningName = new TextInputDialog("Enter Name");
		inputOpeningName.setContentText("Enter the opening Name");
		MenuItem newOpeningWhite = new MenuItem("New White Opening");
		newOpeningWhite.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Optional<String> result = inputOpeningName.showAndWait();
				IOpening o = new OpeningFactory().getOpening();
				o.setName(result.get());
				o.setColor(ChessColors.White);
				data.addOpening(o);
				update();
			}
		});
		this.menuCreationWhite.getItems().add(newOpeningWhite);

		this.menuCreationBlack = new ContextMenu();

		MenuItem newOpeningBlack = new MenuItem("New Black Opening");
		newOpeningBlack.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Optional<String> result = inputOpeningName.showAndWait();
				IOpening o = new OpeningFactory().getOpening();
				o.setName(result.get());
				o.setColor(ChessColors.Black);
				data.addOpening(o);
				update();
			}
		});
		this.menuCreationBlack.getItems().add(newOpeningBlack);
		
		//===========Menu pour les Ouvertures====================
		this.menuOpening = new ContextMenu();
		MenuItem deleteOpening = new MenuItem("Delete");
		deleteOpening.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				data.removeOpening(tree.getSelectionModel().getSelectedItem().getValue());
				update();
			}
		});
		this.menuOpening.getItems().add(deleteOpening);
		
		MenuItem renameOpening = new MenuItem("Rename");
		renameOpening.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Optional<String> newName = inputOpeningName.showAndWait();
				tree.getSelectionModel().getSelectedItem().getValue().setName(newName.get());
				update();
			}
		});
		this.menuOpening.getItems().add(renameOpening);
		
		MenuItem importOpening = new MenuItem("Import Pgn");
		importOpening.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				PgnImportHandler handler = new PgnImportHandler(getScene().getWindow());
				IGame newGame = handler.handle();
				if(newGame != null){
					try {
						tree.getSelectionModel().getSelectedItem().getValue().merge(newGame);
						update();
						model.change();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		this.menuOpening.getItems().add(importOpening);
		
		
		//=====================================================
		this.tree.setCellFactory(new Callback<TreeView<IOpening>, TreeCell<IOpening>>() {

			@Override
			public TreeCell<IOpening> call(TreeView<IOpening> param) {
				return new TreeCellImpl();
			}
		});

		tree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<IOpening>>() {

			@Override
			public void changed(ObservableValue<? extends TreeItem<IOpening>> observable, TreeItem<IOpening> oldValue,
					TreeItem<IOpening> newValue) {
				if (newValue == null || newValue.getValue() == root || newValue.getValue() == black || newValue.getValue() == white) {
					return;
				}
				model.setGame(newValue.getValue());
			}
		});
		this.getChildren().add(tree);
	}

	public void setData(Database data) {
		this.data = data;
	}

	public void setModel(GameSelectionModel model) {
		this.model = model;
	}

	public void update() {
		TreeItem<IOpening> treeRoot = new TreeItem<>(this.root);
		TreeItem<IOpening> treeBlack = new TreeItem<>(this.black);
		TreeItem<IOpening> treeWhite = new TreeItem<>(this.white);
		this.tree.setRoot(treeRoot);
		treeRoot.setExpanded(true);
		treeBlack.setExpanded(true);
		treeWhite.setExpanded(true);

		treeRoot.getChildren().add(treeWhite);
		treeRoot.getChildren().add(treeBlack);

		for(IOpening o : this.data.getOpenings()){
			if(o.getColor() == ChessColors.White)
				treeWhite.getChildren().add(new TreeItem<IOpening>(o));
			else
				treeBlack.getChildren().add(new TreeItem<IOpening>(o));
		}
	}

	private final class TreeCellImpl extends TreeCell<IOpening> {

		@Override
		public void updateItem(IOpening item, boolean empty) {
			super.updateItem(item, empty);
			if (empty) {
				setText(null);
				setGraphic(null);
			} else {
				setText(getItem() == null ? "" : getItem().getName());
				setGraphic(getTreeItem().getGraphic());
				if (item == white) {
					setContextMenu(menuCreationWhite);
				} else if (item == black) {
					setContextMenu(menuCreationBlack);
				}else if(item != root){
					setContextMenu(menuOpening);
				}
			}
		}
	}
}
