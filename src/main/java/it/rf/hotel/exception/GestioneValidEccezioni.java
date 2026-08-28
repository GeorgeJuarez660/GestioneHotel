package it.rf.hotel.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GestioneValidEccezioni {
	

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
 
    	//String errore = ex.getBindingResult().getFieldError().getDefaultMessage();
    	
    	List<String> errori = new ArrayList<>();
    	
    	for(int i=0; i<ex.getBindingResult().getAllErrors().size(); i++) {
    		errori.add(ex.getBindingResult().getAllErrors().get(i).getDefaultMessage());
    	}

        return ResponseEntity.badRequest().body(errori);
    }
	

}
