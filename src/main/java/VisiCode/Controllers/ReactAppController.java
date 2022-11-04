package LocationSearch.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ReactAppController {
    @RequestMapping(value = { "/", "/login", "/register", "/map", "/error", "/profile" })
    public String getIndex(HttpServletRequest request) {
        return "/index.html";
    }
}
