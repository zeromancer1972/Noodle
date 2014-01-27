package org.openntf.noodle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import lotus.domino.Document;
import lotus.domino.NotesException;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class Survey {
	private Document doc;

	public Survey(Document doc) {
		this.doc = doc;
	}

	@SuppressWarnings("unchecked")
	public void getOptions() {
		TreeMap<String, String> map = new TreeMap<String, String>();
		try {
			if (this.doc.isNewNote()) {
				map.put("1", "");
				map.put("2", "");
			} else {

				Vector v = this.doc.getItemValue("surveyOptions");
				for (int x = 0; x < v.size(); x++) {
					map.put(String.valueOf(x), v.elementAt(x).toString());
					ExtLibUtil.getSessionScope().put("option" + String.valueOf(x), v.elementAt(x).toString());
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		ExtLibUtil.getSessionScope().put("options", map);
	}

	@SuppressWarnings("unchecked")
	public void prepareOptions() {
		TreeMap options = (TreeMap) ExtLibUtil.getSessionScope().get("options");
		for (int x = 0; x < 100; x++) {
			String option = (String) ExtLibUtil.getSessionScope().get("option" + String.valueOf(x));
			if (option != null) {
				options.put(String.valueOf(x), option);
			}
		}
		ExtLibUtil.getSessionScope().put("options", options);
	}

	@SuppressWarnings("unchecked")
	public void setOptions() {
		this.prepareOptions();
		TreeMap options = (TreeMap) ExtLibUtil.getSessionScope().get("options");
		Vector<String> v = new Vector<String>();
		try {
			@SuppressWarnings("unused")
			Set keys = options.keySet();
			for (Iterator i = options.keySet().iterator(); i.hasNext();) {
				String key = (String) i.next();
				String value = (String) options.get(key);
				if (!value.equals("")) {
					v.add(value);
				}
			}
			this.doc.replaceItemValue("surveyOptions", v);

		} catch (NotesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public void addOption() {
		Map sessionScope = ExtLibUtil.getSessionScope();
		TreeMap options = (TreeMap) sessionScope.get("options");
		options.put(String.valueOf(Integer.parseInt(options.lastKey().toString()) + 1), "");
		sessionScope.put("options", options);
		ExtLibUtil.getSessionScope().put("option" + options.lastKey().toString(), "");
	}

	@SuppressWarnings("unchecked")
	public void removeOption(String key) {
		Map sessionScope = ExtLibUtil.getSessionScope();
		Map options = (TreeMap) sessionScope.get("options");
		options.remove(key);
		sessionScope.put("options", options);
		ExtLibUtil.getSessionScope().put("option" + key, null);
		this.prepareOptions();
		System.out.println(ExtLibUtil.getSessionScope().get("options"));
	}
}
