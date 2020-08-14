package com.example.budgetassistant.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.budgetassistant.R;
import com.example.budgetassistant.TransactionCategories;
import com.example.budgetassistant.models.RecurringTransaction;
import com.example.budgetassistant.models.UserSettings;
import com.example.budgetassistant.repositories.UserSettingsRepository;

import java.text.ParseException;
import java.util.AbstractMap;

import static android.app.Activity.RESULT_OK;

public class ProfilePictureDialog extends AppCompatDialogFragment {

    private View view;
    private ProfilePictureDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        view = View.inflate(getContext(), R.layout.dialog_profile_picture_selection, null);
        builder.setView(view);
        Dialog dialog =  builder.create();
        initDialog();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    private void initDialog(){
        Button takePhotoBtn = view.findViewById(R.id.TakePhotoBtn);
        Button choosePhotoBtn = view.findViewById(R.id.ChoosePhotoBtn);
        ImageView profilePicDialog = view.findViewById(R.id.profilePicDialog);

        profilePicDialog.setImageResource(UserSettingsRepository.getInstance().getSettings().getValue().profilePicture);
        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        choosePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchChoosePictureIntent();
            }
        });
    }

    static final int REQUEST_IMAGE_CAPTURE = 0;
    static final int REQUEST_IMAGE_SELECTION = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    private void dispatchChoosePictureIntent(){
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhotoIntent,REQUEST_IMAGE_SELECTION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView profilePicDialog = view.findViewById(R.id.profilePicDialog);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            profilePicDialog.setImageBitmap(imageBitmap);
        }
        if(requestCode == REQUEST_IMAGE_SELECTION && resultCode == RESULT_OK){
            Uri selectedImage = data.getData();
            profilePicDialog.setImageURI(selectedImage);
        }
    }


    public void setDialogResult(ProfilePictureDialog.ProfilePictureDialogListener listener){
        mListener = listener;
    }

    public interface ProfilePictureDialogListener{
        void applyChanges(String imagePath);
    }

}
