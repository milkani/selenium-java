package lv.acodemy.utils;

import org.monte.media.Format;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;

import java.awt.*;
import java.io.File;

import static org.monte.media.FormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

public class VideoRecorderUtil {

    private static ScreenRecorder screenRecorder;
    private static File videoDir;
    private static String recordedFilePath;

    public static void startRecording(String testName) throws Exception {
        GraphicsConfiguration gc = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();

        videoDir = new File("target/videos");
        if (!videoDir.exists()) {
            videoDir.mkdirs();
        }

        screenRecorder = new CustomScreenRecorder(gc, videoDir, testName);
        screenRecorder.start();
    }

    public static String stopRecording() throws Exception {
        screenRecorder.stop();
        return recordedFilePath;
    }

    private static class CustomScreenRecorder extends ScreenRecorder {
        private final String testName;

        public CustomScreenRecorder(GraphicsConfiguration cfg, File dir, String testName) throws Exception {
            super(cfg, cfg.getBounds(),
                    new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                    new Format(MediaTypeKey, MediaType.VIDEO,
                            EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            DepthKey, 24, FrameRateKey, Rational.valueOf(30),
                            QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
                    null, null, dir);
            this.testName = testName;
        }

        @Override
        protected File createMovieFile(Format fileFormat) {
            String fileName = testName + "-" + System.currentTimeMillis() + ".avi";
            File file = new File(videoDir, fileName);
            recordedFilePath = file.getAbsolutePath();
            return file;
        }
    }
}
