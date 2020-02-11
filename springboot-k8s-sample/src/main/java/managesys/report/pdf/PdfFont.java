package managesys.report.pdf;

import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

/**
 * フォントを表現するクラス
 */
public class PdfFont {
    private PDFont font;
    private float size;

    /**
     * フォントファイル(TTF)の読み込み
     *
     * @param path     ファイルパス
     * @param document PDFドキュメント
     * @throws IOException
     */
    public void load(InputStream stream, PdfDocument document) throws IOException {
        font = PDType0Font.load(document.getDocument(), stream);
    }

    /**
     * PDフォントを取得する
     *
     * @return PDフォント
     */
    public PDFont getPdFont() {
        return font;
    }

    /**
     * フォント名を取得する
     *
     * @return フォント名
     */
    public String getFontName() {
        return font.getName();
    }

    /**
     * フォントサイズを取得する
     *
     * @return フォントサイズ
     */
    public float getFontSize() {
        return size;
    }

    /**
     * フォントサイズを設定する
     *
     * @param size フォントサイズ
     */
    public void setFontSize(float size) {
        this.size = size;
    }

}
