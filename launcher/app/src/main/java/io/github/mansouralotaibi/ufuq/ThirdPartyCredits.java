package io.github.mansouralotaibi.ufuq;

import android.app.Activity;
import android.app.AlertDialog;
import android.widget.TextView;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/** Offline licenses and provider attribution; external links open only on explicit selection. */
final class ThirdPartyCredits {
    static void show(Activity activity) {
        String text;
        try (InputStream in = activity.getAssets().open("third_party_notices.txt")) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096]; int n;
            while ((n=in.read(buffer))!=-1) out.write(buffer,0,n);
            text=new String(out.toByteArray(),StandardCharsets.UTF_8);
        } catch (Exception error) { text=activity.getString(R.string.not_available); }
        AlertDialog dialog=new ThemedDialogBuilder(activity).setTitle(R.string.ufuq_credits)
                .setMessage(text).setPositiveButton(android.R.string.ok,null).show();
        TextView body=dialog.findViewById(android.R.id.message);
        if(body!=null) {
            android.text.util.Linkify.addLinks(body,android.text.util.Linkify.WEB_URLS);
            body.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
        }
    }
    private ThirdPartyCredits() {}
}
