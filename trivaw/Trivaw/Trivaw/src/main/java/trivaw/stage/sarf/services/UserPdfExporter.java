 package trivaw.stage.sarf.services;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import trivaw.stage.sarf.Entities.User;
import trivaw.stage.sarf.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;

 public class UserPdfExporter {
@Autowired
    UserRepository userRepository;
    User acc ;
    public UserPdfExporter(User acc) {
        this.acc = acc;
    }
    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.PINK);
        cell.setPadding(5);
        com.lowagie.text.Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.BLACK);
        cell.setPhrase(new Phrase("Username", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Email", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("typeCredit", font));
        table.addCell(cell);
        // cell.setPhrase(new Phrase("openDate", font));
        //  table.addCell(cell);
    }
    private void writeTableData(PdfPTable table) {
        table.addCell(acc.getUsername());
        table.addCell(acc.getEmail());
        table.addCell("Agriculture");
              //    String pattern = "yyyy-MM-dd";
	         //     SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    }
    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A3);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(15);
        font.setColor(Color.BLACK);
        Paragraph p = new Paragraph("Extrait de Compte ", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p);
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {2f, 2f, 2f});
        table.setSpacingBefore(10);
        writeTableHeader(table);
        writeTableData(table);
        document.add(table);
        document.close();
    }
}