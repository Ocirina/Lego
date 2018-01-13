package be.tomcools.javaboost.vernie;

import be.tomcools.javaboost.commands.GatttoolCommandWrapper;
import be.tomcools.javaboost.commands.Motor;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import static be.tomcools.javaboost.EventBusConstants.VERNIE;

public class VernieVerticle extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(VernieVerticle.class);
    private static final GatttoolCommandWrapper WRAPPER = new GatttoolCommandWrapper();

    @Override
    public void start() throws Exception {
        super.start();
        vertx.eventBus().consumer(VERNIE, this::executeCommand);
    }

    private void executeCommand(Message<String> tMessage) {
        if (!WRAPPER.isIsKeepingAlive()) {
            WRAPPER.startKeepAlive();
        }

        try {
            String command = tMessage.body();
            switch (command) {
                case "FORWARD":
                    WRAPPER.motorAngle(Motor.AB, 360, 100);
                    break;
                case "BACKWARD":
                    WRAPPER.motorAngle(Motor.AB, 360, -100);
                    break;
                case "TURN RIGHT":
                    WRAPPER.motorAngle(Motor.A, 360, 100);
                    break;
                case "PIVOT RIGHT":
                    WRAPPER.motorAngleMulti(240, 100, -100);
                    break;
                case "TURN LEFT":
                    WRAPPER.motorAngle(Motor.B, 360, 100);
                    break;
                case "PIVOT LEFT":
                    WRAPPER.motorAngleMulti(240, -100, 100);
                    break;
                case "FIRE":
                    WRAPPER.motorAngle(Motor.C, 100, 100);
                    Thread.sleep(1000);
                    WRAPPER.motorAngle(Motor.C, 100, -100);
                    break;
                default:
                    LOG.warn("Invalid command...");
            }
        } catch (Exception ex) {
            tMessage.fail(0, ex.getMessage());
        }
        tMessage.reply("ok");
    }
}