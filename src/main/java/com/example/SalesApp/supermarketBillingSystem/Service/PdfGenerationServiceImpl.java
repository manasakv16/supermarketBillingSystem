package com.example.SalesApp.supermarketBillingSystem.Service;


import com.example.SalesApp.supermarketBillingSystem.Entity.Product;
import com.example.SalesApp.supermarketBillingSystem.Entity.Sales;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Service
public class PdfGenerationServiceImpl implements PdfGenerationService{

    // generate pdf using pdf box - apache
    public byte[] generateBillPdf(final String billContent) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                getDiskCacheFile();
                contentStream.setFont(PDType1Font.COURIER, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 700);
                contentStream.showText(billContent);
                contentStream.endText();
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // initialize disk cache to avoid delay
    private File getDiskCacheFile()
    {
        String path = System.getProperty("pdfbox.fontcache");
        if (path == null)
        {
            path = System.getProperty("user.home");
            if (path == null)
            {
                path = System.getProperty("java.io.tmpdir");
            }
        }
        return new File(path, ".pdfbox.cache");
    }

    // generate pdf using iText
    public byte[] generateBillPdf(final List<Object> objectList) {

        final Document document = new Document();
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            final PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);
            writer.setPageEvent(new PdfPageEventHelper() {
                @Override
                public void onEndPage(PdfWriter writer, Document document) {
                    PdfContentByte canvas = writer.getDirectContentUnder();
                    Rectangle rect = document.getPageSize();
                    rect.setBackgroundColor(new BaseColor(117, 156, 217));
                    canvas.rectangle(rect);
                }
            });
            document.open();

            final Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLACK);
            final Paragraph title = new Paragraph("Invoice", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            final Image image = Image.getInstance("app_logo.png");
            image.scaleToFit(100, 100);
            image.setAlignment(Element.ALIGN_RIGHT);
            document.add(image);

            final Sales sales = (Sales) objectList.get(0);
            addUserData(document, "Sales Date: " + sales.getSalesDate());
            addUserData(document, "Sales ID: " + sales.getSalesId());
            addUserData(document, "Customer ID: " + sales.getCustomerId());
            addUserData(document, "");
            addUserData(document, "User Cart: ");
            addUserData(document, "");
            addUserData(document, "");

            final PdfPTable table = new PdfPTable(objectList.size());
            table.setWidthPercentage(80);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Add table headers
            addTableHeader(table, "Name");
            addTableHeader(table, "Unit");
            addTableHeader(table, "Unit cost");
            addTableHeader(table, "Total cost");

            // Add table rows
            objectList.remove(objectList.get(0));
            for(final Object product: objectList) {
                Product product1 = (Product) product;
                addTableRows(table, product1.getProductName());
                addTableRows(table, product1.getProductUnit().toString());
                addTableRows(table, product1.getProductCost().toString());
                addTableRows(table, product1.getTotalProductCost().toString());
            }

            document.add(table);
            document.add(new Paragraph(""));
            document.add(new Paragraph(""));
            final Font contentFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.NORMAL, BaseColor.BLACK);
            final Paragraph total = new Paragraph("Total: " + sales.getTotal(), contentFont);
            total.setAlignment(Element.ALIGN_CENTER);
            final Paragraph message = new Paragraph("*** Thank you for shopping with us ***", contentFont);
            message.setAlignment(Element.ALIGN_CENTER);
            document.add(total);
            document.add(message);

            final Image endImage = Image.getInstance("thumsUp.jfif");
            endImage.scaleToFit(100, 100);
            endImage.setAlignment(Element.ALIGN_RIGHT);
            document.add(endImage);
            document.close();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public void addTableHeader(PdfPTable table, String data) {
        final Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        final PdfPCell cell = new PdfPCell(new Phrase(data, headerFont));
        cell.setBackgroundColor(BaseColor.DARK_GRAY);
        table.addCell(cell);
    }

    public void addTableRows(PdfPTable table, String data) {
        final Font cellFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
        final PdfPCell cell = new PdfPCell(new Phrase(data, cellFont));
        table.addCell(cell);
    }

    public void addUserData(Document document, String data) throws DocumentException {
        final Font contentFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.NORMAL, BaseColor.BLACK);
        final Paragraph paragraph = new Paragraph(data, contentFont);
        paragraph.setAlignment(Element.ALIGN_LEFT);
        document.add(paragraph);
    }

    // make table using pdfBox
    private void addTableHeader(PdfPTable table) {
        Stream.of("Product Name", "Product unit", "Unit cost", "Total cost")
                .forEach(columnTitle -> {
                    final PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table) {
        table.addCell("row 1, Maggi");
        table.addCell("row 1, 5");
        table.addCell("row 1, 12");
        table.addCell("row 1, 60");
    }

    private void addCustomRows(PdfPTable table)
            throws URISyntaxException, BadElementException, IOException {
        final Path path = Paths.get(ClassLoader.getSystemResource("app_logo.png").toURI());
        final Image img = Image.getInstance(path.toAbsolutePath().toString());
        img.scalePercent(10);

        PdfPCell imageCell = new PdfPCell(img);
        table.addCell(imageCell);

        PdfPCell horizontalAlignCell = new PdfPCell(new Phrase("row 2, col 2"));
        horizontalAlignCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(horizontalAlignCell);

        PdfPCell verticalAlignCell = new PdfPCell(new Phrase("row 2, col 3"));
        verticalAlignCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        table.addCell(verticalAlignCell);
    }

}
