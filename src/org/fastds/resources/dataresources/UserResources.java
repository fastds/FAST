package org.fastds.resources.dataresources;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.fastds.model.User;
import org.fastds.service.DockerService;
import org.fastds.service.UserService;
import org.glassfish.jersey.server.mvc.Viewable;

@Path("/v1")
public class UserResources {
	@Context
	HttpServletRequest request;
	@Context
	HttpServletResponse response;
	private UserService userService;

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Viewable login(@FormParam("name") String name,
			@FormParam("password") String password,
			@FormParam("verifyCode") String verifyCode) throws IOException {
		// 1
		String sessionVerifyCode = (String) request.getSession().getAttribute(
				"session_vcode");
		if (!verifyCode.toLowerCase().equals(sessionVerifyCode)) {
			// System.out.println("verifyCode is not right");
			request.setAttribute("error0", "Verify Code is not right");
			return new Viewable("/login", null);
		}

		// 2
		userService = new UserService();
		User user = userService.findByUsername(name);
		if (user == null) {
			request.setAttribute("error0", name + "does not exits");
			return new Viewable("/login", null);
		} else if (!user.getPassword().equals(password)) {
			request.setAttribute("error0", "password is not right");
			return new Viewable("/login", null);
		}
		String ip = new DockerService().getIp(user.getUserName());
		request.getSession().setAttribute("ip", ip);
		request.getSession().setAttribute("user", user);
		return new Viewable("/index", null);

	}

	@GET
	@Path("loginout")
	public Viewable loginOut() {
		if (request.getSession().getAttribute("user") != null)
			request.getSession().removeAttribute("user");
		return new Viewable("/login", null);
	}

	@POST
	@Path("userUpdate")
	public Viewable update(@BeanParam User user) {
		// 1. 获取表单数据，封装到Customer中 2.

		userService = new UserService();
		String psw = userService.findById(user.getId()).getPassword();
		user.setPassword(psw);
		userService.update(user);
		User userUpdate = userService.findById(user.getId());
		request.getSession().removeAttribute("user");
		request.getSession().setAttribute("user", userUpdate);
		return new Viewable("/profile", null);
	}

	@POST
	@Path("pswUpdate")
	public Viewable updatePsw(@FormParam("id") int id,
			@FormParam("newPassword") String password) {
		System.out.println(id);
		System.out.println(password);
		userService = new UserService();
		if (userService.findById(id).getPassword().equals(password)) {
			request.setAttribute("error", "新旧密码不能一致，请重新填写");
			return new Viewable("/modifypsw", null);
		}
		User user = new User();
		user.setId(id);
		user.setPassword(password);

		userService.updatePassword(user);
		return new Viewable("/modifypsw", null);
	}

	// test
	@POST
	@Path("updatePswPage")
	public void updatePswPage(@FormParam("email") String email)
			throws IOException {
		// User use = (User) request.getSession().getAttribute("user");
		System.out.println(email);
		System.out.println("heheheheheheheheh");
		String str = "okmamda";
		PrintWriter out = response.getWriter();
		out.print(str);
		out.flush();
		out.close();
	}

	@POST
	@Path("register")
	public Viewable userRegister(@BeanParam User user) {
		System.out.println("name:::::" + user);
		userService = new UserService();
		boolean checkName = userService.checkName(user.getUserName());
		if (checkName == true) {
			userService.add(user);
			new DockerService().createContainerWithName(user.getUserName());
			return new Viewable("/login", null);
		} else {
			request.setAttribute("msg", user.getUserName()
					+ "  is already used");
			request.setAttribute("showReg", true);
			return new Viewable("/login", null);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@POST
	@Path("uploade")
	public void uploadeFile() {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			String savePath = "/home/123/Desktop/upload";
			File f1 = new File(savePath);
			if (!f1.exists()) {
				f1.mkdirs();
			}
			DiskFileItemFactory fac = new DiskFileItemFactory(
					500 * 1024 * 1024, new File("/home/123/Desktop/temp"));
			ServletFileUpload upload = new ServletFileUpload(fac);
			upload.setHeaderEncoding("utf-8");
			List fileList = null;
			try {
				fileList = upload.parseRequest(request);
			} catch (FileUploadException ex) {
				return;
			}
			Iterator<FileItem> it = fileList.iterator();
			String name = "";
			while (it.hasNext()) {
				FileItem item = it.next();
				if (!item.isFormField()) {
					name = item.getName();
					System.out.println(name);
					// long size = item.getSize();

					// String type = item.getContentType();
					// System.out.println(size / 1024 / 1024 + "M:::" + type);
					if (name == null || name.trim().equals("")) {
						continue;
					}
					File file = null;
					do {
						file = new File(f1, name);
					} while (file.exists());
					File saveFile = new File(f1, name);
					try {
						item.write(saveFile);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			request.setAttribute("msg", " please login first");
		}
	}
}
