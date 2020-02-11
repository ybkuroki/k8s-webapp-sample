package managesys.report.pdf;

import java.awt.Color;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDPage;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.HorizontalAlignment;
import be.quodlibet.boxable.Row;
import be.quodlibet.boxable.VerticalAlignment;
import be.quodlibet.boxable.line.LineStyle;

/**
 * PDFテーブルを表現するクラス
 */
public class PdfTable {

    private BaseTable table;
    private Row<PDPage> currentRow;
    private PdfDocument document;

    private float yStart;
    private float yStartNewPage;
    private float bottomMargin;
    private float width;

    /**
     * コンストラクタ
     *
     * @param document PDFドキュメント
     * @param margin   余白
     * @throws IOException
     */
    public PdfTable(PdfDocument document, float margin) throws IOException {
        this.document = document;
        this.yStart = document.getPageHight() - (2 * margin);
        this.yStartNewPage = yStart;
        this.bottomMargin = margin;
        this.width = document.getPageWidth() - (2 * margin);

        table = new BaseTable(
                    yStart, yStartNewPage, bottomMargin, width, margin,
                    document.getDocument(), document.getCurrentPage(), true, true
                );
    }

    /**
     * 行を追加する
     *
     * @param height   行の高さ
     * @param isHeader ヘッダーかどうか
     */
    public void createRow(float height, boolean isHeader) {
        currentRow = table.createRow(height);
        if (isHeader)
            table.addHeaderRow(currentRow);
    }

    /**
     * セルを追加する
     *
     * @param width     セルの幅
     * @param value     セルの値
     * @param backColor セルの背景色
     */
    public void createCell(float width, String value, Color backColor) {
        Cell<PDPage> cell = currentRow.createCell(width, value);
        cell.setFont(document.getFont().getPdFont());
        cell.setFontSize(document.getFont().getFontSize());
        // 垂直位置
        cell.setValign(VerticalAlignment.MIDDLE);
        // 水平位置
        cell.setAlign(HorizontalAlignment.LEFT);
        // 線のスタイル
        cell.setTopBorderStyle(new LineStyle(Color.BLACK, 10));
        // 長い文字列を右端で折り返す
        cell.setValign(VerticalAlignment.MIDDLE);
        // 背景色
        cell.setFillColor(backColor);
    }

    /**
     * テーブルを描画する
     *
     * @throws IOException
     */
    public void drawTable() throws IOException {
        table.draw();
    }

    /**
     * セルを取得する
     *
     * @param rowNum    行数
     * @param columnNum 列数
     * @return セル
     */
    public Cell<PDPage> getCell(int rowNum, int columnNum) {
        Row<PDPage> row = getRow(rowNum);
        return row.getCells().get(columnNum);
    }

    /**
     * 行を取得する
     *
     * @param rowNum 行数
     * @return 行
     */
    public Row<PDPage> getRow(int rowNum) {
        return table.getRows().get(rowNum);
    }

}
