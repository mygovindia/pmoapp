package com.sanskrit.pmo.Fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Utils.TouchImageView;
import com.sanskrit.pmo.network.mygov.models.gallery.ImageGallerySubModel;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

public class GalleryPagerFragment extends Fragment {
    private static String GALLERY = "gallery";
    TextView caption;
    TouchImageView image;

    public static Fragment newInstance(ImageGallerySubModel gallery) {
        GalleryPagerFragment f = new GalleryPagerFragment();
        Bundle b = new Bundle();
        b.putParcelable(GALLERY, Parcels.wrap(gallery));
        f.setArguments(b);
        return f;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gallery_pager_backup, container, false);
        this.image = (TouchImageView) rootView.findViewById(R.id.pager_image);
        this.caption = (TextView) rootView.findViewById(R.id.image_text);
        final ImageGallerySubModel gallery = (ImageGallerySubModel) Parcels.unwrap(getArguments().getParcelable(GALLERY));

       /* this.caption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    NewsFeed feed = new NewsFeed();
                    feed.mCOntent = gallery.mCaption;
                    feed.mDate = "";
                    feed.mTitle = "";
                    Intent intent = new Intent(getActivity(), NewsReaderActivity.class);
                    intent.putExtra("newsitem", Parcels.wrap(feed));
                    intent.putExtra("showShare", false);
                    getActivity().startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });*/

        this.caption.setText(gallery.mCaption);
        Picasso.with(getActivity()).load(gallery.mUrl).placeholder((int) R.drawable.ic_image_black_36dp).error((int) R.drawable.ic_image_black_36dp).into(this.image);
        return rootView;
    }
}
