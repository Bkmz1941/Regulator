package ru.bkmz1994.http.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bkmz1994.http.dto.Response;
import ru.bkmz1994.http.exceptions.classes.RegulatorException;
import ru.bkmz1994.http.requests.RegulatorSetTemperatureRequest;
import ru.bkmz1994.http.utils.RegulatorOperationByteParser;
import ru.regulator.ExampleRegulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/regulator")
public class RegulatorController {
    private static final Logger logger = LoggerFactory.getLogger(RegulatorController.class);

    @PostMapping("/set")
    public ResponseEntity<Response<Object>> setTemperature(@RequestBody RegulatorSetTemperatureRequest payload) {
        List<Float> history = new ArrayList<>();
        int code = ExampleRegulator.of().adjustTemp((byte) 0b01100011, payload.getTemperature(), history, 0);
        RegulatorCodeHandle.handle(code);
        Response<Object> response = Response.builder()
                .data(new HashMap<>() {{
                    put("temperature", history.get(0));
                }})
                .message("Success")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/last")
    public ResponseEntity<Response<Object>> getLastTemperature() {
        List<Float> history = new ArrayList<>();
        int code = ExampleRegulator.of().adjustTemp((byte) 0b00100011, 0, history, 0);
        RegulatorCodeHandle.handle(code);
        Response<Object> response = Response.builder()
                .data(new HashMap<>() {{
                    put("temperature", history.get(0));
                }})
                .message("Success")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/history")
    public ResponseEntity<Response<Object>> getTemperatureHistory(@RequestParam int limit, @RequestParam int offset) {
        byte operation = RegulatorOperationByteParser.getTemperatureHistoryOperation(limit);
        List<Float> history = new ArrayList<>();
        int code = ExampleRegulator.of().adjustTemp(operation, 0, history, offset);
        RegulatorCodeHandle.handle(code);
        Response<Object> response = Response.builder()
                .data(new HashMap<>() {{
                    put("history", history);
                    put("limit", limit);
                    put("offset", offset);
                }})
                .message("Success")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/history/clear")
    public ResponseEntity<Response<Object>> getTemperatureHistory() {
        List<Float> history = new ArrayList<>();
        int code = ExampleRegulator.of().adjustTemp((byte) 0b10100011, 0, history, 0);
        RegulatorCodeHandle.handle(code);
        Response<Object> response = Response.builder()
                .data(null)
                .message("Success")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private static class RegulatorCodeHandle {
        public static void handle(int code) {
            if (code == 0) return;
            if (code == 3) throw new RegulatorException("Wrong input data");
            throw new RegulatorException("Something went wrong");
        }
    }
}
