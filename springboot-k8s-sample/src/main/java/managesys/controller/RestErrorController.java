package managesys.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

@RestController
public class RestErrorController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @Autowired
    ErrorAttributes errorAttribute;

    @RequestMapping(ERROR_PATH)
    public Map<String, Object> error(ServletWebRequest request) {
        return this.errorAttribute.getErrorAttributes(request, false);
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

}
