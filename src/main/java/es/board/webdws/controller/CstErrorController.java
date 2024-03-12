package es.board.webdws.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/*
*   This class handles the specific error the website comes across, and shows a different page depending on its status code.
*
*   Class : CstErrorController
*   Implements : ErrorController
*       Function : handleError
*       Param : request
*       return : specific error page for the status code resolved.
*
 */

@Controller
public class CstErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest req){
        Object status = req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null){
            Integer status_code = Integer.valueOf(status.toString());

            if (status_code == HttpStatus.NOT_FOUND.value()){
                return "../static/error/404";
            }
            else if (status_code == HttpStatus.INTERNAL_SERVER_ERROR.value()){
                return "../static/error/500";
            }
            else if (status_code == HttpStatus.UNAUTHORIZED.value()){
                return "../static/error/401";
            }
            else if (status_code == HttpStatus.FORBIDDEN.value()){
                return "../static/error/403";
            }
        }

        return "../static/error/error";
    }
}
