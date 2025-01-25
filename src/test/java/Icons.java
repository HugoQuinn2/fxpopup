import io.github.hugoquinn2.fxpopup.constants.FxPopIcon;
import io.github.hugoquinn2.fxpopup.service.SVGLoader;
import io.github.hugoquinn2.fxpopup.utils.SVGUtil;
import javafx.scene.shape.SVGPath;
import org.junit.jupiter.api.Test;

public class Icons {
    @Test
    void extractSVG() {
        SVGPath icon = SVGUtil.getIcon(FxPopIcon.MINUS_SQUARE, 1);
        System.out.println(icon.getContent());
    }
}
