package chessfx.ui;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ExpandTreeButton extends ImageView {
	private Image expandedImg = new Image("/images/expanded.png", 12, 12, true, true);
	private Image notExpandedImg = new Image("/images/notExpanded.png", 12, 12, true, true);

	private boolean isExpanded = false;
	List<Integer> lineId = new ArrayList<>();

	public ExpandTreeButton() {
		this.setExpanded(false);
	}

	public boolean isExpanded() {
		return isExpanded;
	}

	public void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
		if (this.isExpanded) {
			this.setImage(expandedImg);
		} else {
			this.setImage(notExpandedImg);
		}
	}

	public List<Integer> getLineId() {
		return lineId;
	}
}
