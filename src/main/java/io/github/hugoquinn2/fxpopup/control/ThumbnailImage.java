package io.github.hugoquinn2.fxpopup.control;

import io.github.hugoquinn2.fxpopup.controller.FxPopup;
import io.github.hugoquinn2.fxpopup.utils.MasterUtils;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;

public class ThumbnailImage extends ImageView {
    FxPopup fxPopup;
    ImageView thumbImage;
    Overlay overlay;

    // Thumbnail config
    double scaleThumbImage = 0.6;
    boolean showThumbnail = true;

    // Thumbnail classes for CSS styles
    String DEFAULT_THUMB_IMAGE = "thumbnail-image";

    // Constructor null, path and Image, for customization
    public ThumbnailImage() {this(null, null);}
    public ThumbnailImage(String s) {this(s, null);}
    public ThumbnailImage(Image image) {this(null, image);}

    // General constructor class
    private ThumbnailImage(String s, Image image) {
        fxPopup = new FxPopup();
        thumbImage = new ImageView();
        overlay = new Overlay();

        // Set classes
        thumbImage.getStyleClass().add(DEFAULT_THUMB_IMAGE);

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
            fxPopup.showAll(
                    Pos.CENTER,
                    overlay,
                    thumbImage
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
