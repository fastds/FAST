package org.fastds.resources.dataresources.imageResources;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;

import org.fastds.service.ImageService;
import org.glassfish.jersey.server.mvc.Viewable;

import edu.gzu.domain.JpegInfo;
import edu.gzu.domain.PhotoObjAll;
import edu.gzu.image.ImgCutout;
import edu.gzu.image.SdssConstants;
import edu.gzu.utils.Page;

@Path("/v1")
public class ImageResource {
	@Context
	HttpServletRequest request;
	@Context
	HttpServletResponse response;

	private ImageService service = new ImageService();
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param ra 赤经，范围：[0°-360]，单位：度
	 * @param dec 赤纬，范围：[-90,90]，单位：度
	 * @param opt 绘制选项
	 * @param scaleStr 缩放比例，范围：[0.015,60.0],单位：arcsec/pixel
	 * @return
	 */
	@GET
	@Path("/image/Jpeg")
	public StreamingOutput getJpeg(@QueryParam("ra") double ra,
			@QueryParam("dec") double dec, @QueryParam("opt") String opt,
			@QueryParam("scale") String scaleStr) {

		double scale = 0.396127; // pixel scale in arcsec/pixel [0.015,60.0]
		int height = 120; // pixel height of image
		int width = 120; // pixel width of image
		String param = "";
		String query = "";
		String imgType = "jpeg";
		String imgField = "";

		Map<String, String> errors = new HashMap<String, String>();
		// 解析用户参数，如果错误则保存返回
		try {
			ra = Double.parseDouble(request.getParameter("ra"));
			dec = Double.parseDouble(request.getParameter("dec"));
			String heightStr = request.getParameter("height");
			String widthStr = request.getParameter("width");
			if (scaleStr != null && !scaleStr.trim().isEmpty())
				scale = Double.parseDouble(scaleStr);
			if (heightStr != null && !heightStr.trim().isEmpty())
				height = Integer.parseInt(heightStr);
			if (widthStr != null && !widthStr.trim().isEmpty())
				width = Integer.parseInt(widthStr);
		} catch (Exception e) {
			errors.put(
					"errors",
					"missing parameter: "
							+ param
							+ "ra must be in [0,360], dec must be in [-90,90], scale must be in [0.015, 60.0], height and width must be in [64,2048]");
			request.setAttribute("erros", errors);
			// return "f:/listinfo.jsp";
			// return new Viewable("/listinfo", null);
			return null;
		}

		if (request.getParameter("opt") != null)
			opt = request.getParameter("opt");
		if (request.getParameter("query") != null)
			query = request.getParameter("query");
		opt += 'C';
		// Invoke web method and return its response.
		try {
			ImgCutout cutout = new ImgCutout();
			final byte[] buffer = cutout.GetJpeg(ra, dec, scale, height, width,
					opt, query, imgType, imgField);
			response.setContentType("image/jpeg");
			response.getOutputStream().write(buffer);
			return new StreamingOutput() {
				public void write(OutputStream outputStream) {
					try {
						outputStream.write(buffer);
					} catch (IOException e) {
						try {
							outputStream.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						e.printStackTrace();
					}
				}
			};
		} catch (Exception e) {
			e.printStackTrace();
			String message = e.getMessage();
			message = message.replace('\n', ' ');
			errors.put("errors", message);
			request.setAttribute("erros", errors);
			// return "f:/listinfo.jsp"; // It would be good to pass the
			// exception message
			// return new Viewable("/listinfo", null);
			return null;
		}
	}
	/**
	 * 
	 * @param cp 当前页码
	 * @param opt 绘制参数
	 * @param scaleStr 缩放比例，范围：[0.015,60.0],单位：arcsec/pixel
	 * @param paste 用户发送的查询参数，多个(ra，dec)对
	 * @return
	 */
	@POST
	@Path("/image/JpegList")
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Viewable getJpegList(@FormParam("cp") String cp,
			@FormParam("opt") String opt, @FormParam("scale") String scaleStr,
			@FormParam("paste") String paste) {

		Map<String, String> errors = new HashMap<String, String>();
		Map<String, String[]> params = request.getParameterMap();
		List<JpegInfo> infoList = new ArrayList<JpegInfo>();
		for (String key : params.keySet()) {
			if (!key.equals("paste"))
				System.out.println(key + ".." + params.get(key)[0]);
		}
		int currentPage = cp == null ? 1 : Integer.parseInt(cp);// 当前页 //
																// String opt =
																// request.getParameter("opt");
		double scale = Double.parseDouble(scaleStr);
		String[] arr = paste.replace('\n', ':').split(":");
		if (arr.length <= 1) {
			return new Viewable("/listinfo", null);
		}
		// 初始化page对象
		Page page = new Page();
		page.setTotalRecord(arr.length - 1);
		page.setPageSize(25);
		page.setCurrentPage(currentPage);
		int totalPages = page.getTotalPages();

		// 分割name ra dec
		String regex = " +";
		Pattern pattern = Pattern.compile(regex);
		int start = 25 * (currentPage - 1);
		for (int index = start; index < (arr.length - 1) && index < start + 25; index++) {
			String[] res = pattern.split(arr[index + 1]);
			// System.out.println(Arrays.toString(res));
			if (res.length != 3) {
				errors.put("paramError", "Please check your data!");//提交的数据有误
				break;
			}
			String url = request.getContextPath() + "/v1/image/Jpeg?ra="
					+ res[1] + "&dec=" + res[2] + "&opt=" + opt + "&scale="
					+ scale;
			JpegInfo info = new JpegInfo();
			info.setUrl(url);
			info.setInfo(res[0]);
			info.setRa(Double.parseDouble(res[1]));
			info.setDec(Double.parseDouble(res[2]));
			infoList.add(info);
		}
		/*
		 * 如果产生错误，见错误信息转发到listinfo.jsp页面
		 */
		if (errors.size() > 0) {
			request.setAttribute("errors", errors);
			return new Viewable("/listinfo", null);
		}
		// 保存本页记录到page
		page.setJpegInfo(infoList);
		request.setAttribute("scale", scale);
		request.setAttribute("page", page);
		request.setAttribute("paste", paste);// textarea 内容
		request.setAttribute("opt", opt);
		return new Viewable("/list", null);
	}

	@GET
	@Path("/image/SpecById/{specObjID}")
	public Viewable getSpecById(@PathParam("specObjID") String specObjID) {
		long id = Long.parseLong(specObjID);

		StringBuilder url = new StringBuilder();
		url.append("/navi.jsp?");
		Set<String> keySet = request.getParameterMap().keySet();
		Map<String, String[]> reqMap = request.getParameterMap();
		for (String key : keySet) {
			String val = reqMap.get(key)[0];
			url.append(key + "=" + val + "&");
		}
		byte[] buffer = service.getSpecObjById(id);
		Map<String, String> errors = new HashMap<String, String>();
		if (buffer == null || buffer.length <= 0) {
			errors.put("noimage", "Image not Found!");
		}
		if (errors.size() > 0) {
			request.setAttribute("errors", errors);
//			return new Viewable(url.substring(0,url.length()-1), null);
			return new Viewable("blank", null);
		}
		response.setContentType("image/gif");
		try {
			response.getOutputStream().write(buffer, 0, buffer.length);
		} catch (IOException e) {

			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 搜索距离给定坐标（ra,dec）最近的天体
	 * @param ra 赤经，范围：[0°-360]，单位：度
	 * @param dec 赤纬，范围：[-90,90]，单位：度
	 * @param opt 绘制选项
	 * @param scaleStr 缩放比例，范围：[0.015,60.0],单位：arcsec/pixel
	 * @param radius 半径
	 * @return
	 */
	@GET
	@Path("/image/Nearest")
	public Viewable getNearest(@QueryParam("ra") String ra,
			@QueryParam("dec") String dec, @QueryParam("opt") String opt,
			@QueryParam("scale") String scaleStr,
			@QueryParam("radius") double radius) {
		int height, width;
		height = 120;
		width = 120;
		double scale = 0.396127;

		Map<String, String> errors = new HashMap<String, String>();
		if ((ra == null || ra.isEmpty()) || (dec == null || dec.isEmpty()))
			errors.put("paramError", "Please check your parameters!");//缺少参数

		// if((scaleStr!=null&&!scaleStr.isEmpty())
		// ||(hStr!=null&&!hStr.isEmpty()))
		// {
		// scale = Double.parseDouble(scaleStr);
		// }

		if (errors.size() > 0) {
			request.setAttribute("errors", errors);
			return new Viewable("/blank.jsp"/*?ra="+ra+"&dec="+dec*/, null);

		}
		Map<String, Double> map = getRadius(Double.parseDouble(ra),
				Double.parseDouble(dec), 512, 512, 0.792254);
		String zImageUrl = request.getContextPath() + "/v1/image/Jpeg?ra=" + ra
				+ "&dec=" + dec + "&scale=" + scale;
		PhotoObjAll poa = service.getPhotoObjAll(map.get("ra"), map.get("dec"),
				map.get("radius"));
		long specObjID = service.getSpecObjID(map.get("ra"), map.get("dec"),
				map.get("radius"));
		request.setAttribute("poa", poa);// Primary Object
		request.setAttribute("specObjID", specObjID);
		request.setAttribute("zImageUrl", zImageUrl);
		return new Viewable("/blank.jsp?"/*ra=" + ra + "&dec="+dec*/, null);
	}

	private Map<String, Double> getRadius(double ra_, double dec_, int width,
			int height, double scale) {
		Map<String, Double> map = new HashMap<String, Double>();
		// Normalize ra and dec
		double ra = ra_;
		double dec = dec_;
		dec = dec % 180; // bring dec within the circle
		if (Math.abs(dec) > 90) // if it is "over the pole",
		{
			dec = (dec - 90) % 180; // bring int back to the [-90..90] range
			ra += 180; // and go 1/2 way round the globe
		}
		ra = ra % 360; // bring ra into [0..360]
		if (ra < 0)
			ra += 360;

		double ppd = 3600.0 / scale;
		double radius = 60.0 * 0.5 * Math.sqrt(width * width + height * height)
				/ ppd;
		double fradius = SdssConstants.FrameHalfDiag + radius;// 8.4+
		map.put("ra", ra);
		map.put("dec", dec);
		map.put("radius", radius);
		return map;
	}
	/**
	 * 
	 * @param ra 赤经，范围：[0°-360]，单位：度
	 * @param dec 赤纬，范围：[-90,90]，单位：度
	 * @param opt 绘制选项
	 * @param scaleStr 缩放比例，范围：[0.015,60.0],单位：arcsec/pixel
	 * @param heightStr 高度，单位：像素
	 * @param widthStr 宽度，单位：像素
	 * @return
	 */
	@POST
	@Path("/image/img")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Viewable getImage(@FormParam("ra") double ra,
			@FormParam("dec") final double dec, @FormParam("opt") String opt,
			@FormParam("scale") final double scale,
			@FormParam("height") float heightStr, @FormParam("width") float widthStr) {

		int height = (int) heightStr;
		int width = (int) widthStr;
		request.setAttribute("ra", ra);
		request.setAttribute("dec", dec);
		request.setAttribute("scale", scale);
		request.setAttribute("opt", opt);
		request.setAttribute("height", height);
		request.setAttribute("width", width);
		return new Viewable("/image.jsp", null);

	}

	@GET
	@Path("/hello")
	public StreamingOutput get() throws Exception {
		//
		final byte[] buffer = new byte[24];
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] = (byte) i;
		}
		response.getOutputStream().write(buffer);
		System.out.println("over");
		return new StreamingOutput() {
			public void write(OutputStream outputStream) throws IOException {
				outputStream.write(buffer);
				outputStream.close();
			}
		};
	}

}
