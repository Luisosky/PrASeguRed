package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.dto.CommentRequest;
import co.edu.uniquindio.prasegured.dto.ReportRequest;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    public String createReport(ReportRequest request, String token) {
        // TODO: Implement report creation
        return "generated-report-id"; // Return the ID of the created report
    }

    public void addComment(CommentRequest request, String token) {
        // TODO: Implement comment addition
    }
}