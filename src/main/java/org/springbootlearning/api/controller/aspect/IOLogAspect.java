package org.springbootlearning.api.controller.aspect;  
  
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.ootb.espresso.facilities.ExceptionUtils;
import org.ootb.espresso.facilities.JacksonJSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;

import com.google.common.collect.Maps;


  
//@Aspect
//@Component
//@Order(-100)
public class IOLogAspect {
	
    private Logger logger = LoggerFactory.getLogger(IOLogAspect.class);
    private Logger ioLogger = LoggerFactory.getLogger("ioLogger");
	
//    @Value("#{'${log.ignoreResponseBody}'.split(',')}")
    private List<String> ignoreResponseBodyList;

//    @Value("${log.ignoreResponseBody}")
//    private String ignoreResponseBodyList;

    
    public IOLogAspect(){
    }
    
    @PostConstruct
    public void printConfigInfo(){
        logger.info("IOLogAspect inited,ignoreResponseBodyList:{}",ignoreResponseBodyList);
    }

    
	@Around(value="execution(public * com.xxx.xxx.controller.*Controller.*(..))"
	        + " && @annotation(pm) "
	        + " && args(request,response,data2,..)"
//	        argNames = "joinPoint,pm,data,request,response"
	        )
	public Object printIOLog(ProceedingJoinPoint joinPoint,PostMapping pm, Object data2,
	        HttpServletRequest request,HttpServletResponse response) throws Throwable {

        long st = System.currentTimeMillis();
        long costTimeMs = -1;

        Map<String, String> headersMap = null;
        Map<String, String> paramsMap = null;
        Object result = new String("ExecErr");
        try{
            
            logger.debug("-----------------------------IOLogAspect.printIOLog()-------------------------------------");
            logger.debug("targetClass="+joinPoint.getTarget().getClass());
            logger.debug("targetMethod="+joinPoint.getSignature().getName());
            for(int i=0;i<joinPoint.getArgs().length;i++) {  
                logger.debug("arg["+i+"]="+joinPoint.getArgs()[i]);
            }  
            
            result = joinPoint.proceed();
            costTimeMs = System.currentTimeMillis() - st;
            return result;      
        }   
        catch (Throwable throwable) {
//            logger.error("Err when exec IOLogAspect,errMsg:{}",ExceptionUtils.getStackTrace(throwable,10));
            throw throwable;
        }
        finally {
            try{
                if(needPrintLog(request.getRequestURI())){
                    headersMap = getHeaderJSONMap(request);
                    paramsMap = getParamsJSONMap(request);
                    Map<String,Object> infoMap = Maps.newHashMap();
                    infoMap.put("uri", request.getRequestURI());
                    infoMap.put("headers", headersMap);
                    infoMap.put("params", paramsMap);
                    infoMap.put("body", data2);
                    infoMap.put("result", result);
                    infoMap.put("cost", costTimeMs);
                    ioLogger.info("{}",JacksonJSONUtils.toJSONPretty(infoMap));
                }
            } catch(Throwable throwable){
                logger.error("Err when printIOLog[Fatal],{}",ExceptionUtils.getStackTrace(throwable));
            }
        }
        
	}

    private boolean needPrintLog(String requestURI) {
        if(ignoreResponseBodyList == null || ignoreResponseBodyList.isEmpty()){
            return true;
        }
        
        return !ignoreResponseBodyList.contains(requestURI);
    }

    private Map<String, String> getParamsJSONMap(HttpServletRequest request) {
        Map<String, String> params = Maps.newHashMap();
        Enumeration<String> paramNames = request.getParameterNames();
        while(paramNames.hasMoreElements()){
            String paramName = paramNames.nextElement();
            params.put(paramName, request.getParameter(paramName));
        }
       return params;
    }

    private Map<String, String> getHeaderJSONMap(HttpServletRequest request) {
        Map<String, String> headers = Maps.newHashMap();
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String headerName = headerNames.nextElement();
            if(!"Accept-Charset".equals(headerName)){
                headers.put(headerName, request.getHeader(headerName));
            }
        }
       return headers;
    }
	
} 