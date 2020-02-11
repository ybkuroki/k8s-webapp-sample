package managesys.report.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.util.Matrix;

/**
 * PDFドキュメントを表現するためのクラス
 */
public class PdfDocument {

    private PDDocument document;
    private PDPage currentPage;
    private boolean isRotate;
    private PDPageContentStream stream;
    private PdfFont font;

    /**
     * コンストラクタ
     */
    public PdfDocument() {
        document = new PDDocument();
    }

    /**
     * ページの新規作成
     *
     * @param size     用紙サイズ
     * @param isRotate 用紙向き(true: 横向き)
     * @throws IOException
     */
    public void addPage(PDRectangle size, boolean isRotate) throws IOException {
        this.isRotate = isRotate;
        if (stream != null) {
            stream.close();
        }

        currentPage = new PDPage(PDRectangle.A4);
        if (this.isRotate) {
            currentPage.setRotation(90);
        }
        document.addPage(currentPage);

        // 生成したドキュメントに対して出力するストリームを生成
        stream = new PDPageContentStream(document, currentPage);

        if (this.isRotate) {
            // コンテンツを反時計回りに90度回転させます
            stream.transform(new Matrix(0, 1, -1, 0, currentPage.getMediaBox().getWidth(), 0));
        }
    }

    /**
     * バイト配列として出力する
     *
     * @return バイト配列
     * @throws IOException
     */
    public byte[] save() throws IOException {
        stream.close();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        document.save(byteArrayOutputStream);
        document.close();
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * ファイルとして出力する
     *
     * @param fileName ファイル名
     * @throws IOException
     */
    public void write(String fileName) throws IOException {
        stream.close();

        document.save(fileName);
        document.close();
    }

    /**
     * 用紙の高さを取得する
     *
     * @return 用紙の高さ
     */
    public float getPageHight() {
        if (isRotate) return currentPage.getMediaBox().getWidth();
        return currentPage.getMediaBox().getHeight();
    }

    /**
     * 用紙の幅を取得する
     *
     * @return 用紙の幅
     */
    public float getPageWidth() {
        if (isRotate) return currentPage.getMediaBox().getHeight();
        return currentPage.getMediaBox().getWidth();
    }

    /**
     * 現在の出力ページを取得する
     *
     * @return ページ
     */
    public PDPage getCurrentPage() {
        return currentPage;
    }

    /**
     * ストリームを取得する
     *
     * @return ストリーム
     */
    public PDPageContentStream getContentStream() {
        return stream;
    }

    /**
     * PDドキュメントを取得する
     *
     * @return PDドキュメント
     */
    public PDDocument getDocument() {
        return document;
    }

    /**
     * PDフォントを取得する
     *
     * @return PDフォント
     */
    public PdfFont getFont() {
        return font;
    }

    /**
     * PDFフォントを設定する
     *
     * @param font PDFフォント
     */
    public void setFont(PdfFont font) {
        this.font = font;
    }

}
