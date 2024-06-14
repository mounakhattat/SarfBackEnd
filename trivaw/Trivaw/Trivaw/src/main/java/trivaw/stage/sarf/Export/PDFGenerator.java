package trivaw.stage.sarf.Export;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import trivaw.stage.sarf.Entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;
@Service
public class PDFGenerator {


    private static Logger logger = LoggerFactory.getLogger(PDFGenerator.class);

    public static ByteArrayInputStream employeePDFReport
            (List<User> users) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfWriter.getInstance(document, out);
            document.open();

            // Add Text to PDF file ->
            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 17,
                    BaseColor.BLACK);
            Paragraph para = new Paragraph("All Users", font);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(8);
            // Add PDF Table Header ->
            Stream.of("Id", "FirtName","LastName","Username", "email", "Phone","Housing","Age").forEach(headerTitle ->
            {
                PdfPCell header = new PdfPCell();
                Font headFont = FontFactory.getFont(FontFactory.COURIER);
                header.setBackgroundColor(BaseColor.PINK);
                header.setPhrase(new Phrase(headerTitle, headFont));
                table.addCell(header);

            });

            for (User p : users) {
                PdfPCell idCell = new PdfPCell(new Phrase((String.valueOf(p.getIdUser()))));
                idCell.setPaddingLeft(8);
                idCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                idCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(idCell);

                PdfPCell FirstName = new PdfPCell(new Phrase
                        (p.getFirstName()));
                FirstName.setPaddingLeft(8);
                FirstName.setVerticalAlignment(Element.ALIGN_MIDDLE);
                FirstName.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(FirstName);

                PdfPCell LastName = new PdfPCell(new Phrase
                        (p.getLastName()));
                LastName.setPaddingLeft(8);
                LastName.setVerticalAlignment(Element.ALIGN_MIDDLE);
                LastName.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(LastName);

                PdfPCell Name = new PdfPCell(new Phrase
                        (p.getUsername()));
                Name.setPaddingLeft(8);
                Name.setVerticalAlignment(Element.ALIGN_MIDDLE);
                Name.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(Name);

                PdfPCell Email = new PdfPCell(new Phrase
                        (String.valueOf(p.getEmail())));
                Email.setVerticalAlignment(Element.ALIGN_MIDDLE);
                Email.setHorizontalAlignment(Element.ALIGN_RIGHT);
                Email.setPaddingRight(8);
                table.addCell(Email);

                PdfPCell Phone = new PdfPCell(new Phrase
                        (String.valueOf(p.getNumPhone())));
                Phone.setVerticalAlignment(Element.ALIGN_MIDDLE);
                Phone.setHorizontalAlignment(Element.ALIGN_RIGHT);
                Phone.setPaddingRight(8);
                table.addCell(Phone);

                PdfPCell Housing = new PdfPCell(new Phrase
                        (String.valueOf(p.getPays())));
                Housing.setVerticalAlignment(Element.ALIGN_MIDDLE);
                Housing.setHorizontalAlignment(Element.ALIGN_RIGHT);
                Housing.setPaddingRight(8);
                table.addCell(Housing);

                PdfPCell Age = new PdfPCell(new Phrase
                        (String.valueOf(p.getAge())));
                Age.setVerticalAlignment(Element.ALIGN_MIDDLE);
                Age.setHorizontalAlignment(Element.ALIGN_RIGHT);
                Age.setPaddingRight(8);
                table.addCell(Age);

            }
            document.add(table);

            document.close();
        } catch (DocumentException e) {
            logger.error(e.toString());
        }


        return new ByteArrayInputStream(out.toByteArray());


    }

}
