package info.mrconst.qlient;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.StringTokenizer;

import info.mrconst.qlient.data.MorseTable;

public class ToolFragment extends Fragment {
    EditText mMainInput;
    TextView mBrailleLatView;
    TextView mBrailleCyrView;
    TextView mLatinText;
    TextView mCyrillicText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tool, null);

        mMainInput = (EditText)v.findViewById(R.id.main_input);

        Typeface braille = FontManager.getTypeface(FontManager.BRAILLE);
        mBrailleLatView = (TextView)v.findViewById(R.id.braille_lat_text);
        mBrailleLatView.setTypeface(braille);
        mBrailleCyrView = (TextView)v.findViewById(R.id.braille_cyr_text);
        mBrailleCyrView.setTypeface(braille);

        mLatinText = (TextView)v.findViewById(R.id.latin_text);
        mCyrillicText = (TextView)v.findViewById(R.id.cyrillic_text);

        mMainInput.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        MainActivity ctx = (MainActivity)getActivity();
                        if (ctx.getQuestKeyboard().getCurrentLayout() == R.xml.morse_kbd)
                            _demorseficate(s);
                        else
                            _setNormalText(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                });

        ((MainActivity)getActivity()).getQuestKeyboard().registerEditText(mMainInput);
        return v;
    }

    private void _demorseficate(CharSequence s) {
        StringTokenizer st = new StringTokenizer(s.toString(), " ");
        StringBuilder sbLat = new StringBuilder(st.countTokens());
        StringBuilder sbCyr = new StringBuilder(st.countTokens());

        while(st.hasMoreTokens()) {
            String tok = st.nextToken();
            MorseTable.Symbol symbol = MorseTable.find(tok);
            sbLat.append((symbol != null) ? symbol.lat : "#");
            sbCyr.append((symbol != null) ? symbol.cyr : "#");
        }

        mBrailleLatView.setText(sbLat.toString());
        mLatinText.setText(sbLat.toString());

        mBrailleCyrView.setText(sbCyr.toString());
        mCyrillicText.setText(sbCyr.toString());
    }

    private void _setNormalText(CharSequence s) {
        mBrailleLatView.setText(s);
        mLatinText.setText(s);
        CharSequence cyr = _cyrillize(s);
        mBrailleCyrView.setText(cyr);
        mCyrillicText.setText(cyr);
    }

    private CharSequence _cyrillize(CharSequence s) {
        final String CYR = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
        int count = s.length();
        StringBuilder sb = new StringBuilder(count);
        for(int i = 0; i < count; i++) {
            int cyrId = s.charAt(i) - 'A';
            if (0 <= cyrId && cyrId < CYR.length())
                sb.append(CYR.charAt(cyrId));// вычитаем латинскую А
            else
                sb.append(s.charAt(i));
        }

        return sb.toString();
    }
}
