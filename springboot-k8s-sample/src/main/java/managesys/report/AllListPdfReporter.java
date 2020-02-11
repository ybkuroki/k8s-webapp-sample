package managesys.report;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

import managesys.model.Book;
import managesys.report.pdf.PdfDocument;
import managesys.report.pdf.PdfFont;
import managesys.report.pdf.PdfTable;

/**
 * 書籍一覧表をPDFファイルとして出力するクラス
 */
public class AllListPdfReporter {

    /**
     * 1ページ当たりの最大行数
     */
    private final int MAX_ITEMS_PAR_PAGE = 16;

    /**
     * PDF出力
     *
     * @param datas 出力用のデータ
     * @param file  フォントファイル
     * @return バイトデータ
     */
    public byte[] makeReport(List<Book> datas, InputStream stream) {
        try {
            return createPdf(createData(datas), stream);
        } catch (IOException e) {
            return new byte[0];
        }
    }

    /**
     * PDF生成
     *
     * @param datas 出力用のデータ
     * @param file  フォントファイル
     * @return バイトデータ
     * @throws IOException
     */
    private byte[] createPdf(List<ReportData> datas, InputStream stream) throws IOException {
        PdfDocument doc = new PdfDocument();

        // フォントの読み込み
        PdfFont font = new PdfFont();
        font.load(stream, doc);
        font.setFontSize(10);
        doc.setFont(font);

        // 総ページ数を算出
        int totalPage = 1;
        if (datas.size() > 0) {
            totalPage = (int) Math.ceil((double) datas.size() / (double) MAX_ITEMS_PAR_PAGE);
        }

        // 総ページ数分出力する
        for (int page = 0; page < totalPage; page++) {
            doc.addPage(PDRectangle.A4, true);
            PdfTable table = new PdfTable(doc, 10);

            // ヘッダー生成
            createHeader(table);

            // 最大行数分出力
            for (int i = 0; i < MAX_ITEMS_PAR_PAGE; i++) {
                if (i + page * MAX_ITEMS_PAR_PAGE < datas.size()) {
                    // 奇数行と偶数行でセル背景色を変更
                    if (i % 2 == 0) {
                        createRow(table, datas.get(i + page * MAX_ITEMS_PAR_PAGE), true);
                    } else {
                        createRow(table, datas.get(i + page * MAX_ITEMS_PAR_PAGE), false);
                    }
                }
            }

            table.drawTable();
        }

        return doc.save();
    }

    /**
     * ヘッダー行を出力
     *
     * @param table
     */
    private void createHeader(PdfTable table) {
        table.createRow(20, false);
        table.createCell(10, "書籍ID", Color.LIGHT_GRAY);
        table.createCell(50, "書籍タイトル", Color.LIGHT_GRAY);
        table.createCell(20, "ISBN", Color.LIGHT_GRAY);
        table.createCell(10, "カテゴリ", Color.LIGHT_GRAY);
        table.createCell(10, "形式", Color.LIGHT_GRAY);
    }

    /**
     * テーブル行を出力
     *
     * @param table テーブル
     * @param data  出力データ
     * @param isOdd 偶数行かどうか
     */
    private void createRow(PdfTable table, ReportData data, boolean isOdd) {
        Color fillColor = new Color(217, 225, 242);

        table.createRow(20, false);
        table.createCell(10, data.getId(), isOdd ? fillColor : Color.WHITE);
        table.createCell(50, data.getTitle(), isOdd ? fillColor : Color.WHITE);
        table.createCell(20, data.getIsbn(), isOdd ? fillColor : Color.WHITE);
        table.createCell(10, data.getCategory(), isOdd ? fillColor : Color.WHITE);
        table.createCell(10, data.getFormat(), isOdd ? fillColor : Color.WHITE);
    }

    /**
     * 帳票出力用データを生成
     *
     * @param books DBデータ
     * @return 帳票出力用データ
     */
    private List<ReportData> createData(List<Book> books) {
        List<ReportData> result = new ArrayList<ReportData>();

        books.stream().forEach((book) -> {
            ReportData data = new ReportData(book);
            result.add(data);
        });

        return result;
    }

    /**
     * 帳票データを表す内部クラス
     */
    private static class ReportData {
        private String id = "";
        private String title = "";
        private String isbn = "";
        private String category = "";
        private String format = "";

        /**
         * コンストラクタ
         *
         * @param data DBデータ
         */
        public ReportData(Book data) {
            this.id = String.valueOf(data.getId());
            this.title = data.getTitle();
            this.isbn = data.getIsbn();
            this.category = data.getCategory().getName();
            this.format = data.getFormat().getName();
        }

        // getter
        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getIsbn() {
            return isbn;
        }

        public String getCategory() {
            return category;
        }

        public String getFormat() {
            return format;
        }
    }
}