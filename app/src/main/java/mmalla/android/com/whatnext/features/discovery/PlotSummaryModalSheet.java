package mmalla.android.com.whatnext.features.discovery;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mmalla.android.com.whatnext.R;
import mmalla.android.com.whatnext.model.Movie;

public class PlotSummaryModalSheet extends BottomSheetDialogFragment {

    private final static String TAG = PlotSummaryModalSheet.class.getSimpleName();

    private Movie movie;

    /**
     * Constructor
     */
    public PlotSummaryModalSheet() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.plot_summary_modal_sheet, container, false);

        TextView plot_summary = (TextView) view.findViewById(R.id.plot_summary_text);
        plot_summary.setText(movie.getmOverview());

        return view;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
