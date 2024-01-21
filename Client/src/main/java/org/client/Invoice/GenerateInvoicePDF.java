package org.client.Invoice;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GenerateInvoicePDF {
    public void generateInvoice(Buyer buyer, Seller seller, List<Product> products) throws DocumentException, FileNotFoundException, IOException {
        Document doc = new Document();
        String fileName = buyer.firstName() + "_" + buyer.lastName() + "_" + seller.invoiceNumber() + ".pdf";
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("Invoices\\" + fileName));

        System.out.println("PDF created.");

        doc.open();

        addIssuedInfo(doc);
        addTitle(doc, seller);
        addBuyerAndSellerInfo(doc, buyer, seller);
        addInvoiceProducts(doc, buyer, products);
        addSignatureLines(doc);

        doc.close();
        writer.close();
    }

    private void addIssuedInfo(Document doc) throws DocumentException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String currentDate = dateFormat.format(date);

        Paragraph issue = new Paragraph("Wystawiono dnia " + currentDate + ", Kielce");
        issue.setAlignment(Element.ALIGN_RIGHT);

        doc.add(issue);
        doc.add(Chunk.NEWLINE);
    }

    private void addTitle(Document doc, Seller seller) throws DocumentException, IOException {
        BaseFont baseFont = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(baseFont, 18, Font.BOLD);

        Paragraph title = new Paragraph("Faktura " + seller.invoiceNumber(), font);
        title.setAlignment(Element.ALIGN_CENTER);

        Paragraph date = new Paragraph(seller.date(), font);
        date.setAlignment(Element.ALIGN_CENTER);

        doc.add(title);
        doc.add(date);
        doc.add(Chunk.NEWLINE);
    }

    private void addBuyerAndSellerInfo(Document doc, Buyer buyer, Seller seller) throws DocumentException, IOException {
        BaseFont baseFont = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(baseFont);
        Font fontTitle = new Font(baseFont, 12, Font.BOLD);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(80);

        PdfPCell sellerCell = new PdfPCell();
        sellerCell.addElement(new Paragraph("Sprzedawca:", fontTitle));
        sellerCell.addElement(Chunk.NEWLINE);
        sellerCell.addElement(new Paragraph("Identyfikator: " + seller.id(), font));
        sellerCell.addElement(new Paragraph(seller.firstName() + " " + seller.lastName(), font));

        PdfPCell buyerCell = new PdfPCell();
        buyerCell.addElement(new Paragraph("Nabywca:", fontTitle));
        buyerCell.addElement(Chunk.NEWLINE);
        buyerCell.addElement(new Paragraph(buyer.firstName() + " " + buyer.lastName(), font));
        if (buyer.nip() != null && !buyer.nip().equals("")) {
            buyerCell.addElement(new Paragraph("NIP: " + buyer.nip(), font));
        }
        buyerCell.addElement(new Paragraph(buyer.pesel(), font));
        buyerCell.addElement(new Paragraph(buyer.phoneNumber(), font));
        buyerCell.addElement(new Paragraph(buyer.houseNumber() + " " + buyer.street() + ", " + buyer.city(), font));

        buyerCell.setBorderColor(BaseColor.WHITE);
        sellerCell.setBorderColor(BaseColor.WHITE);

        table.addCell(sellerCell);
        table.addCell(buyerCell);

        table.setHorizontalAlignment(Element.ALIGN_RIGHT);

        doc.add(table);
        doc.add(Chunk.NEWLINE);
    }

    private void addInvoiceProducts(Document doc, Buyer buyer, List<Product> products) throws DocumentException, IOException {
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);

        addTableHeader(table);
        addTableData(table, products);

        doc.add(table);
        doc.add(Chunk.NEWLINE);

        Paragraph totalCost = new Paragraph("Razem: " + buyer.totalCost() + " PLN");
        totalCost.setAlignment(Element.ALIGN_RIGHT);

        doc.add(totalCost);
    }

    private void addTableHeader(PdfPTable table) throws DocumentException, IOException {
        BaseFont baseFont = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(baseFont, 12, Font.BOLD);

        PdfPCell cell0 = new PdfPCell(new Paragraph("Lp.", font));
        PdfPCell cell1 = new PdfPCell(new Paragraph("Produkt", font));
        PdfPCell cell2 = new PdfPCell(new Paragraph("Producent", font));
        PdfPCell cell4 = new PdfPCell(new Paragraph("Ilość", font));
        PdfPCell cell5 = new PdfPCell(new Paragraph("Cena", font));

        table.addCell(cell0);
        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell4);
        table.addCell(cell5);
    }


    private void addTableData(PdfPTable table, List<Product> products) throws DocumentException, IOException {
        BaseFont baseFont = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(baseFont, 12);

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);

            table.addCell(new PdfPCell(new Paragraph(Integer.toString(i + 1), font)));
            table.addCell(new PdfPCell(new Paragraph(product.name(), font)));
            table.addCell(new PdfPCell(new Paragraph(product.producer(), font)));
            table.addCell(new PdfPCell(new Paragraph(product.quantity(), font)));
            table.addCell(new PdfPCell(new Paragraph(product.price(), font)));
        }
    }

    private void addSignatureLines(Document doc) throws DocumentException, IOException {
        BaseFont baseFont = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(baseFont, 12);

        for (int i = 0; i < 4; i++ ) {
            doc.add(Chunk.NEWLINE);
        }

        PdfPTable signatureTable = new PdfPTable(2);
        signatureTable.setWidthPercentage(90);

        PdfPCell authorizedToIssueCell = new PdfPCell();
        authorizedToIssueCell.addElement(new Chunk("____________________________"));
        authorizedToIssueCell.addElement(new Paragraph("Osoba upoważniona do wystawienia", font));

        PdfPCell authorizedToReceiveCell = new PdfPCell();
        authorizedToReceiveCell.addElement(new Chunk("____________________________"));
        authorizedToReceiveCell.addElement(new Paragraph("Osoba upoważniona do odbioru", font));

        authorizedToIssueCell.setBorderColor(BaseColor.WHITE);
        authorizedToReceiveCell.setBorderColor(BaseColor.WHITE);

        signatureTable.addCell(authorizedToIssueCell);
        signatureTable.addCell(authorizedToReceiveCell);

        signatureTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

        doc.add(signatureTable);
    }
}
