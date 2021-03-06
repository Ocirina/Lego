package be.tomcools.javaboost.vernie;

import be.tomcools.javaboost.commands.GatttoolCommandWrapper;
import be.tomcools.javaboost.commands.components.LedColor;
import be.tomcools.javaboost.commands.components.Motor;
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
        WRAPPER.startKeepAlive();
        vertx.eventBus().consumer(VERNIE, this::executeCommand);
    }

    private void executeCommand(Message<String> tMessage) {
        try {
            String command = tMessage.body();
            String[] commands = command.split("-");
            switch (commands[0]) {
                case "FORWARD":
                    WRAPPER.motorAngle(Motor.AB, 360, 100);
                    break;
                case "BACKWARD":
                    WRAPPER.motorAngle(Motor.AB, 360, -100);
                    break;
                case "TURN_RIGHT":
                    WRAPPER.motorAngle(Motor.A, 360, 100);
                    break;
                case "PIVOT_RIGHT":
                    WRAPPER.motorAngleMulti(240, 100, -100);
                    break;
                case "DANCE":
                    WRAPPER.motorAngleMulti(240, 100, -100);
                    WRAPPER.motorAngleMulti(240, -100, 100);
                    WRAPPER.motorAngleMulti(240, -100, 100);
                    WRAPPER.motorAngleMulti(240, 100, -100);
                    WRAPPER.motorAngle(Motor.AB, 360, 20);
                    WRAPPER.motorAngle(Motor.AB, 360, -20);
                    break;
                case "TURN_LEFT":
                    WRAPPER.motorAngle(Motor.B, 360, 100);
                    break;
                case "PIVOT_LEFT":
                    WRAPPER.motorAngleMulti(240, -100, 100);
                    break;
                case "LED":
                    if(commands.length>1) {
                        WRAPPER.led(LedColor.valueOf(commands[1]));
                        break;
                    }
                case "FIRE":
                    WRAPPER.motorAngle(Motor.C, 100, 100);
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
