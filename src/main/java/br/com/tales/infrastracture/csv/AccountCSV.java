package br.com.tales.infrastracture.csv;

import br.com.tales.domain.model.Account;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AccountCSV {
    public static String TYPE = "text/csv";
    static String[] HEADERs = {
            "description", "value", "date_expiration"
    };


    public static boolean hasCSVFormat(MultipartFile file) {
        if (TYPE.equals(file.getContentType())
                || file.getContentType().equals("application/vnd.ms-excel")) {
            return true;
        }

        return false;
    }

    public static List<Account> csvToAccount(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.newFormat(';').withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<Account> accountList = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                accountList.add(new Account(csvRecord));
            }
            return accountList;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao transformar o arquivo CSV: " + e.getMessage());
        }
    }

//    public static ByteArrayInputStream accountToCSV(List<Ubs> ubsList) {
//        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
//
//        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
//             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
//            for (Ubs ubs : ubsList) {
//                List<? extends Serializable> data = Arrays.asList(
//                        String.valueOf(ubs.getId()),
//                        ubs.getVlr_latitude(),
//                        ubs.getVlr_longitude(),
//                        ubs.getCod_munic(),
//                        ubs.getCodCnes(),
//                        ubs.getNom_estab(),
//                        ubs.getDsc_endereco(),
//                        ubs.getDsc_bairro(),
//                        ubs.getDsc_cidade(),
//                        ubs.getDsc_telefone(),
//                        ubs.getDsc_estrut_fisic_ambiencia(),
//                        ubs.getDsc_adap_defic_fisic_idosos(),
//                        ubs.getDsc_equipamentos(),
//                        ubs.getDsc_medicamentos(),
//                        ubs.getLatitude(),
//                        ubs.getLongitude()
//                );
//
//                csvPrinter.printRecord(data);
//            }
//
//            csvPrinter.flush();
//            return new ByteArrayInputStream(out.toByteArray());
//        } catch (IOException e) {
//            throw new RuntimeException("Erro ao importar o arquivo CSV: " + e.getMessage());
//        }
//    }
}
