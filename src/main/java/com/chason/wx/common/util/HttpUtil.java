package com.chason.wx.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HTTP工具类
 * 
 * @author Alex
 */
public class HttpUtil {
	private static final Log log = LogFactory.getLog(HttpUtil.class);

	/**
	 * 发送HTTP GET方式请求，返回请求响应中的字符串，不适应需要post返回响应数据的请求场景
	 * 
	 * @param reqUrl
	 * @return
	 */
	public static String sendHttpGetReqToServer(String reqUrl) {
		if (StringUtils.isEmpty(reqUrl))
			return null;
		long startTime = System.currentTimeMillis();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String result = null;
		String status = null;
		try {
			HttpGet httpGet = new HttpGet(reqUrl);
			CloseableHttpResponse response1 = httpclient.execute(httpGet);
			try {
				if (null != response1) {
					if (null != response1.getStatusLine())
						status = response1.getStatusLine().toString();
					if (null != response1.getEntity())
						result = EntityUtils.toString(response1.getEntity(), StandardCharsets.UTF_8);
				}
			} catch (ParseException e) {
				log.error("向Http服务端发送请求时发生异常，原因：", e);
			} finally {
				if (response1 != null)
					response1.close();
			}
		} catch (IOException e) {
			log.error("向Http服务端发送请求时发生异常，原因：", e);
		} finally {
			if (httpclient != null) {
				try {
					httpclient.close();
				} catch (IOException e) {
					log.error("关闭httpclient时发生异常，原因：", e);
				}
			}
		}
		long endTime = System.currentTimeMillis();
		log.info(String.format("向Http服务端发送请求，请求地址：%s，请求返回状态：%s，请求返回报文：%s，开始时间：%s，结束时间：%s，共耗时(毫秒)：%s", reqUrl, status, result,
				DateTimeUtil.toDateTimeString(startTime, "yyyy-MM-dd HH:mm:ss.S"), DateTimeUtil.toDateTimeString(endTime, "yyyy-MM-dd HH:mm:ss.S"), (endTime - startTime)));
		return result;
	}

	/**
	 * 高性能方式发送请求到服务器
	 * 
	 * @param reqUrl
	 * @return
	 */
	public static String sendHttpGetReqToServerByHighPerformance(String reqUrl) {
		try {
			Content result = Request.Get(reqUrl).execute().returnContent();
			if (null == result)
				return null;
			return result.asString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 发送HTTP post方式请求
	 * 
	 * @param httpServerUrl
	 *            请求地址
	 * @param map
	 *            携带的参数值
	 * @return
	 */
	public static String sendHttpPostReqToServerByParams(String httpServerUrl, Map<String, String> map) {
		if (StringUtils.isEmpty(httpServerUrl))
			return null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String result = null;
		String status = null;
		try {
			HttpPost httpPost = new HttpPost(httpServerUrl);
			if (null != map && !map.isEmpty()) {
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> entry : map.entrySet()) {
					nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
			}
			CloseableHttpResponse response1 = httpclient.execute(httpPost);
			try {
				if (null != response1) {
					if (null != response1.getStatusLine())
						status = response1.getStatusLine().toString();
					if (null != response1.getEntity())
						result = EntityUtils.toString(response1.getEntity(), StandardCharsets.UTF_8);
				}
			} catch (ParseException e) {
				log.error("向Http服务端发送请求时发生异常，原因：", e);
			} finally {
				if (response1 != null)
					response1.close();
			}
		} catch (IOException e) {
			log.error("向Http服务端发送请求时发生异常，原因：", e);
		} finally {
			if (httpclient != null) {
				try {
					httpclient.close();
				} catch (IOException e) {
					log.error("关闭httpclient时发生异常，原因：", e);
				}
			}
		}
		log.info(String.format("向Http服务端发送请求，请求地址：%s，请求报文：%s，请求返回状态：%s，请求返回报文：%s", httpServerUrl, map, status, result));
		return result;
	}

	/**
	 * 发送HTTP post方式请求
	 * 
	 * @param httpServerUrl
	 *            请求地址
	 * @param reqBody
	 *            请求的body字符串
	 * @return
	 */
	public static String sendHttpPostReqToServerByReqbody(String httpServerUrl, String reqBody, String contentType) {
		if (StringUtils.isEmpty(httpServerUrl))
			return null;
		if (StringUtils.isEmpty(contentType))
			contentType = "application/x-www-form-urlencoded";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String result = null;
		String status = null;
		try {
			HttpPost httpPost = new HttpPost(httpServerUrl);
			if (StringUtils.isNotEmpty(reqBody)) {
				StringEntity reqBodyEntity = new StringEntity(reqBody, StandardCharsets.UTF_8);
				reqBodyEntity.setContentType(contentType);
				httpPost.setEntity(reqBodyEntity);
			}
			CloseableHttpResponse response1 = httpclient.execute(httpPost);
			try {
				if (null != response1) {
					if (null != response1.getStatusLine())
						status = response1.getStatusLine().toString();
					if (null != response1.getEntity())
						result = EntityUtils.toString(response1.getEntity(), StandardCharsets.UTF_8);
				}
			} catch (ParseException e) {
				log.error("向Http服务端发送请求时发生异常，原因：", e);
			} finally {
				if (response1 != null)
					response1.close();
			}
		} catch (IOException e) {
			log.error("向Http服务端发送请求时发生异常，原因：", e);
		} finally {
			if (httpclient != null) {
				try {
					httpclient.close();
				} catch (IOException e) {
					log.error("关闭httpclient时发生异常，原因：", e);
				}
			}
		}
		log.info(String.format("向Http服务端发送请求，请求地址：%s，请求报文：%s，请求返回状态：%s，请求返回报文：%s", httpServerUrl, reqBody, status, result));
		return result;
	}

	public static String sendHttpPostReqToServerByStream(String httpServerUrl, Map<String, String> textParams, Map<String, String> fileParams, String contentType) {
		if (StringUtils.isEmpty(httpServerUrl))
			return null;
		if (StringUtils.isEmpty(contentType))
			contentType = "multipart/form-data";

		CloseableHttpClient httpclient = HttpClients.createDefault();
		String result = null;
		String status = null;
		try {
			HttpPost httpPost = new HttpPost(httpServerUrl);

			MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
			multipartEntity.setCharset(StandardCharsets.UTF_8);
			if (null != textParams && !textParams.isEmpty()) {
				for (Map.Entry<String, String> e : textParams.entrySet()) {
					// 解决中文乱码
					multipartEntity.addTextBody(e.getKey(), e.getValue(), ContentType.create("text/plain", StandardCharsets.UTF_8));
				}
			}
			if (null != fileParams && !fileParams.isEmpty()) {
				File file;
				for (Map.Entry<String, String> e : fileParams.entrySet()) {
					file = new File(e.getValue());
					multipartEntity.addBinaryBody(e.getKey(), file, ContentType.APPLICATION_OCTET_STREAM, e.getValue());
				}
			}
			httpPost.setEntity(multipartEntity.build());

			CloseableHttpResponse response1 = null;
			try {
				response1 = httpclient.execute(httpPost);
				if (null != response1) {
					if (null != response1.getStatusLine())
						status = response1.getStatusLine().toString();
					if (null != response1.getEntity())
						result = EntityUtils.toString(response1.getEntity(), StandardCharsets.UTF_8);
				}
			} catch (ParseException e) {
				log.error("向Http服务端发送请求时发生异常，原因：", e);
			} finally {
				if (response1 != null)
					response1.close();
			}
		} catch (IOException e) {
			log.error("向Http服务端发送请求时发生异常，原因：", e);
		} finally {
			if (httpclient != null) {
				try {
					httpclient.close();
				} catch (IOException e) {
					log.error("关闭httpclient时发生异常，原因：", e);
				}
			}
		}
		log.info(String.format("向Http服务端发送请求，请求地址：%s，请求返回状态：%s，请求返回报文：%s", httpServerUrl, status, result));
		return result;
	}

	/**
	 * 高性能方式发送请求到服务器
	 * 
	 * @param reqUrl
	 * @return
	 */
	public static String sendHttpPostStreamReqToServerByHighPerformance(String reqUrl, String keyName, String fileName, InputStream input) {
		try {
			MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
			multipartEntity.setCharset(StandardCharsets.UTF_8);
			multipartEntity.addBinaryBody(keyName, input, ContentType.APPLICATION_OCTET_STREAM, fileName);
			Content result = Request.Post(reqUrl).body(multipartEntity.build()).execute().returnContent();
			if (null == result)
				return null;
			return result.asString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 微信支付专用
	 * 
	 * @param url
	 *            ：请求地址
	 * @param reqBody
	 * @param mch_id
	 *            :微信公众号商户编号
	 * @param carAddress
	 *            ：微信证书
	 * @return
	 */
	public static String mpSendHttpPostReqToServerByReqbody(String url, String reqBody, String mch_id, String carAddress) {
		String returnStr = null;
		try {
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			FileInputStream instream = new FileInputStream(new File(carAddress));
			try {
				keyStore.load(instream, mch_id.toCharArray());
			} finally {
				instream.close();
			}
			// Trust own CA and all self-signed certs
			SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mch_id.toCharArray()).build();
			// Allow TLSv1 protocol only
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
					SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			try {
				HttpPost httppost = new HttpPost(url);
				if (StringUtils.isNotEmpty(reqBody)) {
					StringEntity reqBodyEntity = new StringEntity(reqBody, StandardCharsets.UTF_8);
					httppost.setEntity(reqBodyEntity);
				}
				CloseableHttpResponse response = httpclient.execute(httppost);
				try {
					HttpEntity entity = response.getEntity();
					returnStr = EntityUtils.toString(entity, StandardCharsets.UTF_8);
					EntityUtils.consume(entity);
				} finally {
					response.close();
				}
			} finally {
				httpclient.close();
			}
		} catch (Exception e) {
		}
		return returnStr.toString();
	}

	/**
	 * 微信 上传LOGO图像接口
	 * 
	 * @param uploadImgUrl
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static String uploadImg(String uploadImgUrl, String filePath) throws Exception {
		String result = null;
		File file = new File(filePath);
		if (!file.exists() || !file.isFile()) {
			throw new IOException("文件不存在");
		}
		/**
		 * 第一部分
		 */
		URL urlObj = new URL(uploadImgUrl);
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
		con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false); // post方式不能使用缓存
		// 设置请求头信息
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");
		// 设置边界
		String BOUNDARY = "----------" + System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
		// 请求正文信息
		// 第一部分：
		StringBuilder sb = new StringBuilder();
		sb.append("--"); // 必须多两道线
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");
		byte[] head = sb.toString().getBytes("utf-8");
		// 获得输出流,输出表头
		OutputStream out = new DataOutputStream(con.getOutputStream());
		out.write(head);
		// 文件正文部分
		// 把文件已流文件的方式 推入到url中
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while ((bytes = in.read(bufferOut)) != -1) {
			out.write(bufferOut, 0, bytes);
		}
		in.close();
		// 结尾部分
		byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
		out.write(foot);
		out.flush();
		out.close();
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		try {
			// 定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			if (result == null) {
				result = buffer.toString();
			}
		} catch (IOException e) {
			System.out.println("发送POST请求出现异常！" + e);
			e.printStackTrace();
			throw new IOException("数据读取异常");
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return result;
	}

	public static String SMS(String postData, String postUrl) {
		try {
			// 发送POST请求
			URL url = new URL(postUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setUseCaches(false);
			conn.setDoOutput(true);

			conn.setRequestProperty("Content-Length", "" + postData.length());
			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			out.write(postData);
			out.flush();
			out.close();

			// 获取响应状态
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				System.out.println("connect failed!");
				return "";
			}
			// 获取响应内容体
			String line, result = "";
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			while ((line = in.readLine()) != null) {
				result += line + "\n";
			}
			in.close();
			return result;
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
		return "";
	}

	public static void main(String[] args) {
		sendHttpPostReqToServerByReqbody("http://127.0.0.1/zq-tv-portal/http/test1",
				"{\"action_name\": \"QR_LIMIT_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": {scene_id}}}}", "application/json");
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "alex");
		map.put("age", "32");
		sendHttpPostReqToServerByParams("http://127.0.0.1/zq-tv-portal/http/test2", map);
	}
}
