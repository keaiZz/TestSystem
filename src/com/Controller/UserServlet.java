package com.Controller;

import com.Entity.User;
import com.Service.UserService;
import com.Service.UserServiceImpl.UserServiceimpl;
import com.Util.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "UserServlet",urlPatterns = "/userServlet")
public class UserServlet extends BaseServlet {
    //生成服务对象
    UserService userService=new UserServiceimpl();
    public String login(HttpServletRequest request,HttpServletResponse response){
        //接收请求，调用服务，跳转界面
        String username=request.getParameter("username");
        String userPassword=request.getParameter("password");
        System.out.println(username);
        System.out.println(userPassword);
        System.out.println("==================");
        //1.校验数据格式
        if (username==""||userPassword==""){
            //在session域中存放返回给用户的信息
            HttpSession session=request.getSession();
            session.setAttribute("Msg","用户名或密码不能为空");
            System.out.println("用户名或密码不能为空");
            return "WEB-INF/login.jsp";
        }
        try {
            //调用服务，取出对应用户名的用户id与密码
            //2.若数据库中无此用户
            User user = userService.queryByUserName(username);
        }catch (Exception e){
            System.out.println("此用户不存在");
            System.out.println("请先注册");
            HttpSession session=request.getSession();
            session.setAttribute("Msg","用户不存在，请先注册!");
            return "WEB-INF/login.jsp";
        }

        User user = userService.queryByUserName(username);
        //3.做密码校验 若密码错误
        if(!user.getPassword().equals(userPassword)){
            HttpSession session=request.getSession();
            session.setAttribute("Msg","密码错误");
            System.out.println("密码错误");
            return "WEB-INF/login.jsp";
        }
        //4.校验通过
        //使用Session存入user信息
        HttpSession session=request.getSession();
        session.setAttribute("User",user);
        request.setAttribute("userName",username);
        return "WEB-INF/Exam/studentIndex.jsp";
    }
    public  String register(HttpServletRequest request,HttpServletResponse response){
        //接收请求，调用服务，跳转界面
        //将前端的数据整合在一个user对象中
        User user=new User();
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("password"));
        user.setName(request.getParameter("name"));
        user.setAge(request.getParameter("age"));
        user.setSex(request.getParameter("sex"));
        user.setRole(request.getParameter("role"));
        user.setTelephone(request.getParameter("telephone"));
        //调用服务
        userService.insert(user);
        //跳转界面
        return "WEB-INF/login.jsp";
    }
    public String studentIndex(HttpServletRequest request,HttpServletResponse response){
        return "WEB-INF/Exam/studentIndex.jsp";
    }
    public String studentRegister(HttpServletRequest request,HttpServletResponse response){
        return "WEB-INF/register.jsp";
    }
}
