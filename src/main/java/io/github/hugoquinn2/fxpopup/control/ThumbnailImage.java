package io.github.hugoquinn2.fxpopup.control;

import io.github.hugoquinn2.fxpopup.utils.MasterUtils;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;

import static io.github.hugoquinn2.fxpopup.config.CssClasses.THUMBNAIL_IMAGE_CLASS;

public class ThumbnailImage extends ImageView {
    ImageView thumbImage;
    Overlay overlay;

    // Thumbnail config
    double scaleThumbImage = 0.8;
    boolean showThumbnail = true;

    // Constructor null, path and Image, for customization

    /**
     * Creates an empty thumbnail image, the thumbnail will not be displayed until set <code>setThumbImage()</code>
     * and <code>ImageView</code>.
     */
    public ThumbnailImage() {
        this(null, null);
    }

    /**
     * Creates thumbnail image, a thumbnail will be displayed when a user click on <code>ImageView</code>
     * @param s the path or url that will be displayed on thumbnail and <code>ImageView</code>.
     */
    public ThumbnailImage(String s) {
        this(s, null);
    }

    /**
     * Creates thumbnail image, a thumbnail will be displayed when a user click on <code>ImageView</code>
     * @param image the image that will be displayed on thumbnail and <code>ImageView</code>.
     */
    public ThumbnailImage(Image image) {
        this(null, image);
    }

    // General constructor class
    private ThumbnailImage(String s, Image image) {
        thumbImage = new ImageView();
        overlay = new Overlay();

        // Set classes
        thumbImage.getStyleClass().add(THUMBNAIL_IMAGE_CLASS);

        if (s != null) {
            this.setImage(new Image(s));
            thumbImage.setImage(new Image(s));
        }
        else if (image != null) {
            this.setImage(image);
            thumbImage.setImage(image);
        }

        setPreserveRatio(true);
        setThumbnail();
    }

    private void setThumbnail() {
        overlay.setOnMouseClicked(event -> {
            MasterUtils.remove(thumbImage);
            MasterUtils.remove(overlay);
        });

        setOnMouseClicked(event3 -> {
            if (!showThumbnail)
                return;

            Rectangle2D screenBounds = Screen.getPrimary().getBounds();
            double screenWidth = screenBounds.getWidth();
            double thumbImageWidth = screenWidth * scaleThumbImage;

            // Resize image to 80% size windows and preserve ratio
            thumbImage.setFitWidth(thumbImageWidth);
            thumbImage.setPreserveRatio(true);

            // Show image above Overlay
            Parent root = MasterUtils.wrapInStackPane(MasterUtils.getRoot());
            StackPane.setAlignment(overlay, Pos.CENTER);
            StackPane.setAlignment(thumbImage, Pos.CENTER);
            ((Pane) root).getChildren().addAll(
                    overlay, thumbImage
            );
        });
    }

    public ImageView getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(ImageView thumbImage) {
        this.thumbImage = thumbImage;
    }

    public Overlay getOverlay() {
        return overlay;
    }

    public void setOverlay(Overlay overlay) {
        this.overlay = overlay;
    }

    public double getScaleThumbImage() {
        return scaleThumbImage;
    }

    public void setScaleThumbImage(double scaleThumbImage) {
        this.scaleThumbImage = scaleThumbImage;
    }

    public boolean isShowThumbnail() {
        return showThumbnail;
    }

    public void setShowThumbnail(boolean showThumbnail) {
        this.showThumbnail = showThumbnail;
    }
}
