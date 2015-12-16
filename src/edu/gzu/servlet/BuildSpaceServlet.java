///**
// * @author jamesmarva jamesmarva@163.com
// * @date Aug 22, 2015 9:51:58 PM
// */
//package edu.gzu.servlet;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import edu.gzu.dao.BesureDataDao;
//import edu.gzu.pojo.UserBuildData;
////import edu.gzu.domain.User;
//import org.fastds.model.User;
//import edu.gzu.service.ControlSpaceService;
///**
// * Servlet implementation class BuildSpaceServlet
// */
//@WebServlet("/BuildSpaceServlet")
//public class BuildSpaceServlet extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//       
//    
//    public BuildSpaceServlet() {
//        super();
//        // TODO Auto-generated constructor stub
//    }
//    
//    /**
//	 * init service to control space
//	 */
//	private ControlSpaceService mControlSpaceService = new ControlSpaceService();
//    
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//	}
//
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		
//		request.setCharacterEncoding("utf-8");
//		response.setContentType("text/html;charset=utf-8");
//		User user = (User) request.getSession().getAttribute("user");
//		Map<String,String> errors = new HashMap<String,String>();
////		String mBuildResult = "";
//		
//		
//		if (user==null) {
//			errors.put("loginInfo", "<font color='red'>请先登录</font>");
//			request.setAttribute("errors",errors);
//			request.getRequestDispatcher("/buildspace.jsp").forward(request, response);
//			return;
//		}
//		
//		/* get value of "status" */
//		String status = request.getParameter("status");
//		System.out.println("status is :" + status);
//		if (status.equals("Build")) {
//			int mBuildResult = mControlSpaceService.firstBuild(user.getUserName());
//			if (mBuildResult == 1) {
//				BesureDataDao mBesureDataDao = new BesureDataDao();
//				UserBuildData mUserBuildData = mBesureDataDao.getIsbuildSpaceStatus(user.getUserName());
//				if (mUserBuildData != null){
//					request.getSession().setAttribute("mUserBuildData", mUserBuildData);
//				}
//				
//				request.setAttribute("mBuildResult", "Init space uccessly. Please click  'Into my Space' to use yourself space.");
//				request.getRequestDispatcher("/buildspace.jsp").forward(request, response);
//		   	 	return;
//			} else {
//				BesureDataDao mBesureDataDao = new BesureDataDao();
//				UserBuildData mUserBuildData = mBesureDataDao.getIsbuildSpaceStatus(user.getUserName());
//				if (mUserBuildData != null){
//					request.getSession().setAttribute("mUserBuildData", mUserBuildData);
//				}
//				
//				request.setAttribute("mBuildResult", "Init space Failed. Please try again later, or contact to us.");
//				request.getRequestDispatcher("/buildspace.jsp").forward(request, response);
//		   	 	return;
//			}
//		} else if (status.equals("Start")) {
//			int mStartResult = mControlSpaceService.startSpace(user.getUserName());
//			
//			if (mStartResult == 1) {
//				BesureDataDao mBesureDataDao = new BesureDataDao();
//				UserBuildData mUserBuildData = mBesureDataDao.getIsbuildSpaceStatus(user.getUserName());
//				if (mUserBuildData != null){
//					request.getSession().setAttribute("mUserBuildData", mUserBuildData);
//				}
//				
//				request.setAttribute("mStartResult", "Start space success.");
//				request.getRequestDispatcher("/buildspace.jsp").forward(request, response);
//		   	 	return;
//			} else {
//				BesureDataDao mBesureDataDao = new BesureDataDao();
//				UserBuildData mUserBuildData = mBesureDataDao.getIsbuildSpaceStatus(user.getUserName());
//				if (mUserBuildData != null){
//					request.getSession().setAttribute("mUserBuildData", mUserBuildData);
//				}
//				
//				request.setAttribute("mStartResult", "Start space unsuccess.");
//				request.getRequestDispatcher("/buildspace.jsp").forward(request, response);
//		   	 	return;
//			}
//			
//		} else if (status.equals("Stop")) {
//			int mStopResult = mControlSpaceService.stopSpace(user.getUserName());
//			
//			if ( mStopResult == 1) {
//				BesureDataDao mBesureDataDao = new BesureDataDao();
//				UserBuildData mUserBuildData = mBesureDataDao.getIsbuildSpaceStatus(user.getUserName());
//				if (mUserBuildData != null){
//					request.getSession().setAttribute("mUserBuildData", mUserBuildData);
//				}
//				
//				request.setAttribute("mStopResult", "Stop space successsly.");
//				request.getRequestDispatcher("/buildspace.jsp").forward(request, response);
//		   	 	return;
//			} else {
//				BesureDataDao mBesureDataDao = new BesureDataDao();
//				UserBuildData mUserBuildData = mBesureDataDao.getIsbuildSpaceStatus(user.getUserName());
//				if (mUserBuildData != null){
//					request.getSession().setAttribute("mUserBuildData", mUserBuildData);
//				}
//				
//				request.setAttribute("mStopResult", "Stop space unsuccessly.");
//				request.getRequestDispatcher("/buildspace.jsp").forward(request, response);
//		   	 	return;
//			}
//		}
//	}
//
//}
