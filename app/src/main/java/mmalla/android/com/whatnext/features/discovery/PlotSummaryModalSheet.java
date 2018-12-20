package mmalla.android.com.whatnext.features.discovery;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mmalla.android.com.whatnext.R;

public class PlotSummaryModalSheet extends BottomSheetDialogFragment {

    /**
     * Constructor
     */
    public PlotSummaryModalSheet() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.plot_summary_modal_sheet, container, false);
        return view;
    }
}
