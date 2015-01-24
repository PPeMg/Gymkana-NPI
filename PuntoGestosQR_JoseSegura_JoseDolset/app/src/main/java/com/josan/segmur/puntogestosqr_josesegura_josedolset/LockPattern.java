package com.josan.segmur.puntogestosqr_josesegura_josedolset;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class LockPattern extends Activity{
	private static final int REQUEST_CODE_SET_LOCK_PATTERN = 10001;
	private static final int REQUEST_CODE_VERIFY_LOCK_PATTERN = 10002;

	protected PatternView mView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//
		mView = new PatternView(this);
		//
		String savedData = LockPatternUtils.loadFromPreferences(this);
		if (savedData == null) {
			Intent intent = new Intent(this, SetLockPatternActivity.class);
			startActivityForResult(intent, REQUEST_CODE_SET_LOCK_PATTERN);
		} else {
			Intent intent = new Intent(this, VerifyLockPatternActivity.class);
			startActivityForResult(intent, REQUEST_CODE_VERIFY_LOCK_PATTERN);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CODE_SET_LOCK_PATTERN:
			if (resultCode == Activity.RESULT_OK) {
				mView.updateView(PatternView.STATUS_LOCK_PATTERN_SAVED);
			} else {
				mView.updateView(PatternView.STATUS_LOCK_PATTERN_NOT_SAVED);
			}
			break;

		case REQUEST_CODE_VERIFY_LOCK_PATTERN:
			if (resultCode == Activity.RESULT_OK) {
				Intent i = new Intent(this, QReader.class );
		        startActivity(i);
			} else {
				mView.updateView(PatternView.STATUS_LOCK_PATTERN_VERFIED_FAILED);
			}
			break;
		}
	}
}
