package VisiCode.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ReactAppController {
    @RequestMapping(value = { "/", "/login", "/register", "/projects", "/projects/**" })
    public String getIndex(HttpServletRequest request) {
        return "/index.html";
    }
}
