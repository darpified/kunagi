package scrum.client.journal;

import ilarkesto.core.base.Str;
import ilarkesto.gwt.client.AGwtEntity;

import java.util.Comparator;
import java.util.Map;

import scrum.client.impediments.Impediment;
import scrum.client.issues.Issue;
import scrum.client.risks.Risk;
import scrum.client.risks.RiskComputer;

public class Change extends GChange {

	public Change(Map data) {
		super(data);
	}

	public String getLabel() {
		return getFieldChangeLabel();
	}

	private String getFieldChangeLabel() {
		String key = getKey();
		AGwtEntity parent = getParent();
		String oldValue = getOldValue();
		String newValue = getNewValue();

		if (parent instanceof Issue) {
			if (key.equals("closeDate")) return Str.isEmpty(newValue) ? "reopened issue" : "closed issue";
		} else if (parent instanceof Impediment) {
			if (key.equals("closed")) return Str.isTrue(newValue) ? "closed impediment" : "reopened impediment";
		}

		if (Str.isEmpty(oldValue)) return "created " + getFieldLabel();
		if (Str.isEmpty(newValue)) return "deleted " + getFieldLabel();
		return "changed " + getFieldLabel();
	}

	public String getDiff() {
		String key = getKey();
		AGwtEntity parent = getParent();
		String oldValue = getOldValue();
		String newValue = getNewValue();

		if (parent instanceof Issue) {
			if (key.equals("closeDate")) return null;
		} else if (parent instanceof Impediment) {
			if (key.equals("closed")) return null;
		} else if (parent instanceof Risk) {
			if (key.equals("impact"))
				return createSinglelineDiff(RiskComputer.getImpactLabel(oldValue), RiskComputer
						.getImpactLabel(newValue));
			if (key.equals("probability"))
				return createSinglelineDiff(RiskComputer.getProbabilityLabel(oldValue), RiskComputer
						.getProbabilityLabel(newValue));
		}

		if (Str.isEmpty(oldValue)) return getNewValue();
		if (Str.isEmpty(newValue)) return null;
		return createMultilineDiff(oldValue, newValue);
	}

	private String getFieldLabel() {
		String key = getKey();
		return key;
	}

	@Override
	public String toString() {
		return getUser() + " on " + getDateAndTime() + ": " + getParent() + " ." + getKey();
	}

	public static transient final Comparator<Change> DATE_AND_TIME_COMPARATOR = new Comparator<Change>() {

		public int compare(Change a, Change b) {
			return b.getDateAndTime().compareTo(a.getDateAndTime());
		}
	};

	private static String createSinglelineDiff(Object oldValue, Object newValue) {
		return oldValue + "<code> -> </code>" + newValue;
	}

	private static String createMultilineDiff(String oldValue, String newValue) {
		return oldValue + "\n\n          < old | new >\n\n" + newValue;
	}

}