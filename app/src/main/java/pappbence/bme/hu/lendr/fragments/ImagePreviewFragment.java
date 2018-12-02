package pappbence.bme.hu.lendr.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment;
import pappbence.bme.hu.lendr.BitmapUtil;
import pappbence.bme.hu.lendr.R;

public class ImagePreviewFragment extends SupportBlurDialogFragment {

    public static final String TAG = "ImagePreviewFragment";

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.image_preview_title)
                .setView(getContentView())
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.image_preview_dialog, null);
        ImageView previewImageView = contentView.findViewById(R.id.previewImageView);
        byte[] byteArray = getArguments().getByteArray("bytearray");
        Bitmap imageBitmap = BitmapUtil.ByteArrayToBitmap(byteArray);
        previewImageView.setImageBitmap(imageBitmap);
        return contentView;
    }
}
