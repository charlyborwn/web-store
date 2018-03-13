package pti.test.server;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * This class corresponds for generic exception handling of controllers.
 *
 * @author Syrotyuk R.
 */
@ControllerAdvice(value = {"pti.test.server","pti.test.controller"})
public class ExceptionsHandler {

    /**
     * Generic exception handling method.
     *
     * @param exception exception to be handled
     * @param request   HTTP request
     * @return ModelAndView object that represent the exception view
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    public ModelAndView handleGenericException(Exception exception, HttpServletRequest request) throws Exception {
        return createModelAndView(request, HttpStatus.BAD_REQUEST, exception);
    }

    /**
     * Builds a ModelAndView object filled with exception handling parameters
     * and maps it to correspondent error view.
     *
     * @param request   HTTP request
     * @param status    HTTP status to be added to ModelAndView
     * @param exception exception that was happened
     * @return a ModelAndView object filled with exception handling parameters mapped to correspondent error view
     * @throws Exception
     */
    private ModelAndView createModelAndView(HttpServletRequest request, HttpStatus status, Exception exception) throws Exception {
        if (AnnotationUtils.findAnnotation(exception.getClass(), ResponseStatus.class) != null) {
            throw exception;
        }
        ModelAndView model = new ModelAndView();
        model.addObject("exception", exception.getClass().getSimpleName());
        model.addObject("message", exception.getMessage());
        model.addObject("url", request.getRequestURL());
        model.addObject("date", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        model.addObject("code", status);
        model.addObject("status", status.getReasonPhrase());
        model.setViewName("error");
        return model;
    }

}
