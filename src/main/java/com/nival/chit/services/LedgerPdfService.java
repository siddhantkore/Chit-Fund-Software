package com.nival.chit.services;

import com.nival.chit.entity.Ledger;
import com.nival.chit.repository.LedgerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LedgerPdfService {

//    private final LedgerRepository ledgerRepository;
//
//    /**
//     *
//     */
//    public void generateLedgerPdf() {
//        List<Ledger> ledgerList = ledgerRepository.findAll();
//
//    }
////    @Service
////    public class LedgerPdfService {
//
////        public byte[] generateLedgerPd//package com.nival.chit.services;
//
//import com.nival.chit.entity.Ledger;
//import com.nival.chit.repository.LedgerRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.awt.*;
//import java.io.ByteArrayOutputStream;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class LedgerPdfService {
//f(List<Ledger> entries) throws Exception {
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            Document document = new Document();
//            PdfWriter.getInstance(document, out);
//
//            document.open();
//
//            // Title
//            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
//            Paragraph title = new Paragraph("Ledger Report", titleFont);
//            title.setAlignment(Element.ALIGN_CENTER);
//            document.add(title);
//
//            document.add(new Paragraph("Generated On: " + LocalDateTime.now()));
//            document.add(new Paragraph(" ")); // space
//
//            // Table
//            PdfPTable table = new PdfPTable(4);
//            table.setWidthPercentage(100);
//
//            table.addCell("ID");
//            table.addCell("Entry Type");
//            table.addCell("Amount");
//            table.addCell("Timestamp");
//
//            for (Ledger entry : entries) {
//                table.addCell(String.valueOf(entry.getId()));
//                table.addCell(entry.getEntryType());
//                table.addCell(String.valueOf(entry.getAmount()));
//                table.addCell(entry.getCreatedAt().toString());
//            }
//
//            document.add(table);
//            document.close();
//
//            return out.toByteArray();
//        }
//    }
//
}
