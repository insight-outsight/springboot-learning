//package org.springbootlearning.api.config;
//
//import java.lang.reflect.Field;
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.embedded.undertow.UndertowServletWebServer;
//import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
//import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.event.ContextClosedEvent;
//import org.springframework.stereotype.Component;
//import org.xnio.Options;
//
//import io.undertow.Undertow;
//import io.undertow.UndertowOptions;
//import io.undertow.server.ConnectorStatistics;
//import io.undertow.server.HandlerWrapper;
//import io.undertow.server.HttpHandler;
//import io.undertow.server.handlers.GracefulShutdownHandler;
//
//@Configuration
//public class GracefulShutdownConfig {
//    
//    @Component
//    public class GracefulShutdownUndertow implements ApplicationListener<ContextClosedEvent> {
//     
//        private final Logger logger = LoggerFactory.getLogger(GracefulShutdownUndertow.class);
//        @Autowired
//        private GracefulShutdownUndertowWrapper gracefulShutdownUndertowWrapper;
//     
//        @Autowired
//        private ServletWebServerApplicationContext context;
//     
//        @Override
//        public void onApplicationEvent(ContextClosedEvent contextClosedEvent){
//            try {
//                gracefulShutdownUndertowWrapper.getGracefulShutdownHandler().shutdown();
//                long st = System.currentTimeMillis();
//                logger.info("undertow graceful shutdown start.");
//                UndertowServletWebServer webServer = (UndertowServletWebServer)context.getWebServer();
//                Field field = webServer.getClass().getDeclaredField("undertow");
//                field.setAccessible(true);
//                Undertow undertow = (Undertow) field.get(webServer);
//                List<Undertow.ListenerInfo> listenerInfo = undertow.getListenerInfo();
//                Undertow.ListenerInfo listener = listenerInfo.get(0);
//                ConnectorStatistics connectorStatistics = listener.getConnectorStatistics();
//                //默认大概会等待120s，这里限制到30s
//                while (connectorStatistics.getActiveConnections() > 0 && (System.currentTimeMillis()-st < 35000)){
//                    
//                }
//                logger.info("undertow graceful shutdown end.");
//            }catch (Exception e){
//                logger.error("",e);
//            }
//        }
//    }
//    
//    @Component
//    public class GracefulShutdownUndertowWrapper implements HandlerWrapper {
//        private GracefulShutdownHandler gracefulShutdownHandler;
//        @Override
//        public HttpHandler wrap(HttpHandler handler) {
//            if(gracefulShutdownHandler == null) {
//                this.gracefulShutdownHandler = new GracefulShutdownHandler(handler);
//            }
//            return gracefulShutdownHandler;
//        }
//        public GracefulShutdownHandler getGracefulShutdownHandler() {
//            return gracefulShutdownHandler;
//        }
//    }
//    
//    @Component
//    public class GracefulShudownUndertowConfiguration {
//        @Autowired
//        private GracefulShutdownUndertowWrapper gracefulShutdownUndertowWrapper;
//        @Bean
//        public UndertowServletWebServerFactory servletWebServerFactory() {
//            UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
//            factory.addDeploymentInfoCustomizers(deploymentInfo -> deploymentInfo.addOuterHandlerChainWrapper(gracefulShutdownUndertowWrapper));
//            factory.addBuilderCustomizers(builder -> {
//              builder.setServerOption(UndertowOptions.ENABLE_STATISTICS, true);
//              builder.setBufferSize(1024 * 16 - 20);
//              builder.setDirectBuffers(true);
//              builder.setSocketOption(Options.TCP_NODELAY,true);
//              builder.setSocketOption(Options.REUSE_ADDRESSES,true);
//              builder.setSocketOption(Options.SEND_BUFFER,1024*32);
//              builder.setSocketOption(Options.RECEIVE_BUFFER,1024*32);
//              builder.setSocketOption(UndertowOptions.BUFFER_PIPELINED_DATA,true);
//            });
//            return factory;
/**
 * 
 * server.undertow.accesslog.dir=./logs/access_log  //日志路径
server.undertow.accesslog.enabled=true //开启accesslog
server.undertow.accesslog.pattern=%t %a "%r" %s (%D ms) 
... https://www.cnblogs.com/shareTechnologyl/articles/10757907.html
也就是说必须要开启这个配置，才能在访问日志里打印响应时间，至于undertow为什么不默认开启这个配置，是因为开启后会对性能造成影响
在spring boot(2.0)要开启这个配置需要通过代码进行配置:
        factory.addBuilderCustomizers(new UndertowBuilderCustomizer() {
            @Override
            public void customize(Undertow.Builder builder) {
                builder.setServerOption(UndertowOptions.RECORD_REQUEST_START_TIME, true);
            }
        });
        **/
//        }
//    }
//    
//}
