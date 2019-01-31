import java.io.*;
import java.util.List;

import javax.servlet.*;
import javax.servlet.http.*;

import com.google.gson.Gson;

import javax.servlet.annotation.*;

@WebServlet(urlPatterns = {"/"})
public class MainServlet extends HttpServlet {
    private Instaget service;

    @Override
    public void init()
    {
        this.service = new Instaget();
    }

    @Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String url = req.getParameter("url");
        if (url != null) {
            List<String> imageUrls = service.getImageUrls(url);
            res.setContentType("application/json");
            PrintWriter out = res.getWriter();
            Gson gson = new Gson();
            out.print(gson.toJson(imageUrls));
            out.close();
        }
    }
    
    @Override
    public void destroy()
    {
        service.close();
    }
}
