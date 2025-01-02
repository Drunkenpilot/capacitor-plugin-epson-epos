package be.betalife.plugin.capacitor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.Epos2CallbackCode;

public class ShowMsg {
    public static void showException(Exception e, String method, Context context) {
        String msg;
        if (e instanceof Epos2Exception) {
            msg = String.format(
                    "%s\n\t%s\n%s\n\t%s",
                    "Error Code",
                    getEposExceptionText(((Epos2Exception) e).getErrorStatus()),
                    "Method",
                    method);
        } else {
            msg = e.toString();
        }
        show(msg, context);
    }

    public static void showResult(int code, String errMsg, Context context) {
        String msg;
        if (errMsg.isEmpty()) {
            msg = String.format(
                    "\t%s\n\t%s\n",
                    "Result",
                    getCodeText(code));
        } else {
            msg = String.format(
                    "\t%s\n\t%s\n\n\t%s\n\t%s\n",
                    "Result",
                    getCodeText(code),
                    "Description",
                    errMsg);
        }
        show(msg, context);
    }

    public static void showMsg(String msg, Context context) {
        show(msg, context);
    }

    private static void show(final String msg, final Context context) {
        if (context instanceof Activity activity) {
            // 如果是 Activity，正常显示对话框
            new AlertDialog.Builder(activity)
                    .setMessage(msg)
                    .setPositiveButton("OK", null)
                    .show();
        } else {
            // 如果是 ApplicationContext，可以考虑使用 Toast
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }

    }

    @SuppressLint("DefaultLocale")
    private static String getEposExceptionText(int state) {
        return switch (state) {
            case Epos2Exception.ERR_PARAM -> "ERR_PARAM";
            case Epos2Exception.ERR_CONNECT -> "ERR_CONNECT";
            case Epos2Exception.ERR_TIMEOUT -> "ERR_TIMEOUT";
            case Epos2Exception.ERR_MEMORY -> "ERR_MEMORY";
            case Epos2Exception.ERR_ILLEGAL -> "ERR_ILLEGAL";
            case Epos2Exception.ERR_PROCESSING -> "ERR_PROCESSING";
            case Epos2Exception.ERR_NOT_FOUND -> "ERR_NOT_FOUND";
            case Epos2Exception.ERR_IN_USE -> "ERR_IN_USE";
            case Epos2Exception.ERR_TYPE_INVALID -> "ERR_TYPE_INVALID";
            case Epos2Exception.ERR_DISCONNECT -> "ERR_DISCONNECT";
            case Epos2Exception.ERR_ALREADY_OPENED -> "ERR_ALREADY_OPENED";
            case Epos2Exception.ERR_ALREADY_USED -> "ERR_ALREADY_USED";
            case Epos2Exception.ERR_BOX_COUNT_OVER -> "ERR_BOX_COUNT_OVER";
            case Epos2Exception.ERR_BOX_CLIENT_OVER -> "ERR_BOX_CLIENT_OVER";
            case Epos2Exception.ERR_UNSUPPORTED -> "ERR_UNSUPPORTED";
            case Epos2Exception.ERR_FAILURE -> "ERR_FAILURE";
            default -> String.format("%d", state);
        };
    }

    @SuppressLint("DefaultLocale")
    private static String getCodeText(int state) {
        return switch (state) {
            case Epos2CallbackCode.CODE_SUCCESS -> "PRINT_SUCCESS";
            case Epos2CallbackCode.CODE_PRINTING -> "PRINTING";
            case Epos2CallbackCode.CODE_ERR_AUTORECOVER -> "ERR_AUTORECOVER";
            case Epos2CallbackCode.CODE_ERR_COVER_OPEN -> "ERR_COVER_OPEN";
            case Epos2CallbackCode.CODE_ERR_CUTTER -> "ERR_CUTTER";
            case Epos2CallbackCode.CODE_ERR_MECHANICAL -> "ERR_MECHANICAL";
            case Epos2CallbackCode.CODE_ERR_EMPTY -> "ERR_EMPTY";
            case Epos2CallbackCode.CODE_ERR_UNRECOVERABLE -> "ERR_UNRECOVERABLE";
            case Epos2CallbackCode.CODE_ERR_FAILURE -> "ERR_FAILURE";
            case Epos2CallbackCode.CODE_ERR_NOT_FOUND -> "ERR_NOT_FOUND";
            case Epos2CallbackCode.CODE_ERR_SYSTEM -> "ERR_SYSTEM";
            case Epos2CallbackCode.CODE_ERR_PORT -> "ERR_PORT";
            case Epos2CallbackCode.CODE_ERR_TIMEOUT -> "ERR_TIMEOUT";
            case Epos2CallbackCode.CODE_ERR_JOB_NOT_FOUND -> "ERR_JOB_NOT_FOUND";
            case Epos2CallbackCode.CODE_ERR_SPOOLER -> "ERR_SPOOLER";
            case Epos2CallbackCode.CODE_ERR_BATTERY_LOW -> "ERR_BATTERY_LOW";
            case Epos2CallbackCode.CODE_ERR_TOO_MANY_REQUESTS -> "ERR_TOO_MANY_REQUESTS";
            case Epos2CallbackCode.CODE_ERR_REQUEST_ENTITY_TOO_LARGE ->
                    "ERR_REQUEST_ENTITY_TOO_LARGE";
            case Epos2CallbackCode.CODE_CANCELED -> "CODE_CANCELED";
            case Epos2CallbackCode.CODE_ERR_NO_MICR_DATA -> "ERR_NO_MICR_DATA";
            case Epos2CallbackCode.CODE_ERR_ILLEGAL_LENGTH -> "ERR_ILLEGAL_LENGTH";
            case Epos2CallbackCode.CODE_ERR_NO_MAGNETIC_DATA -> "ERR_NO_MAGNETIC_DATA";
            case Epos2CallbackCode.CODE_ERR_RECOGNITION -> "ERR_RECOGNITION";
            case Epos2CallbackCode.CODE_ERR_READ -> "ERR_READ";
            case Epos2CallbackCode.CODE_ERR_NOISE_DETECTED -> "ERR_NOISE_DETECTED";
            case Epos2CallbackCode.CODE_ERR_PAPER_JAM -> "ERR_PAPER_JAM";
            case Epos2CallbackCode.CODE_ERR_PAPER_PULLED_OUT -> "ERR_PAPER_PULLED_OUT";
            case Epos2CallbackCode.CODE_ERR_CANCEL_FAILED -> "ERR_CANCEL_FAILED";
            case Epos2CallbackCode.CODE_ERR_PAPER_TYPE -> "ERR_PAPER_TYPE";
            case Epos2CallbackCode.CODE_ERR_WAIT_INSERTION -> "ERR_WAIT_INSERTION";
            case Epos2CallbackCode.CODE_ERR_ILLEGAL -> "ERR_ILLEGAL";
            case Epos2CallbackCode.CODE_ERR_INSERTED -> "ERR_INSERTED";
            case Epos2CallbackCode.CODE_ERR_WAIT_REMOVAL -> "ERR_WAIT_REMOVAL";
            case Epos2CallbackCode.CODE_ERR_DEVICE_BUSY -> "ERR_DEVICE_BUSY";
            case Epos2CallbackCode.CODE_ERR_IN_USE -> "ERR_IN_USE";
            case Epos2CallbackCode.CODE_ERR_CONNECT -> "ERR_CONNECT";
            case Epos2CallbackCode.CODE_ERR_DISCONNECT -> "ERR_DISCONNECT";
            case Epos2CallbackCode.CODE_ERR_MEMORY -> "ERR_MEMORY";
            case Epos2CallbackCode.CODE_ERR_PROCESSING -> "ERR_PROCESSING";
            case Epos2CallbackCode.CODE_ERR_PARAM -> "ERR_PARAM";
            case Epos2CallbackCode.CODE_RETRY -> "RETRY";
            case Epos2CallbackCode.CODE_ERR_DIFFERENT_MODEL -> "ERR_DIFFERENT_MODEL";
            case Epos2CallbackCode.CODE_ERR_DIFFERENT_VERSION -> "ERR_DIFFERENT_VERSION";
            case Epos2CallbackCode.CODE_ERR_DATA_CORRUPTED -> "ERR_DATA_CORRUPTED";
            case Epos2CallbackCode.CODE_ERR_JSON_FORMAT -> "ERR_JSON_FORMAT";
            case Epos2CallbackCode.CODE_NO_PASSWORD -> "NO_PASSWORD";
            case Epos2CallbackCode.CODE_ERR_INVALID_PASSWORD -> "ERR_INVALID_PASSWORD";
            case Epos2CallbackCode.CODE_ERR_INVALID_FIRM_VERSION -> "ERR_INVALID_FIRM_VERSION";
            case Epos2CallbackCode.CODE_ERR_SSL_CERTIFICATION -> "ERR_SSL_CERTIFICATION";
            default -> String.format("%d", state);
        };
    }
}
