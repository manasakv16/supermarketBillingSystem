package com.example.SalesApp.supermarketBillingSystem.Service;

import java.util.List;

public interface PdfGenerationService {

    byte[] generateBillPdf(String billContent);

    byte[] generateBillPdf(List<Object> objectList);
}
