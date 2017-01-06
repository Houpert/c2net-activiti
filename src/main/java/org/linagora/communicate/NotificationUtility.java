package org.linagora.communicate;

import java.util.ArrayList;
import java.util.List;

import org.linagora.dao.openpaas.notification.NotificationActionOP;
import org.linagora.dao.openpaas.notification.NotificationLevelOP;
import org.linagora.dao.openpaas.notification.NotificationOP;
import org.linagora.dao.openpaas.notification.NotificationObjectOP;
import org.linagora.dao.openpaas.notification.NotificationObjectTypeOP;
import org.linagora.dao.openpaas.notification.NotificationTargetOP;

public class NotificationUtility {

	public static NotificationOP testNotification() {
		return createNotification("http://localhost:3000/form/myNewNotification", "5757e32933e422073571f0aa",
				"5757e32933e422073571f0aa");
	}

	private static NotificationOP createNotification(String link, String author, String target) {
		List<NotificationTargetOP> targetNotification = new ArrayList<NotificationTargetOP>();
		targetNotification.add(new NotificationTargetOP(NotificationObjectTypeOP.USER, target));

		NotificationOP opNot = new NotificationOP("MyActivitiNotification", NotificationActionOP.CREATED,
				NotificationObjectOP.FORM, link, NotificationLevelOP.TRANSIENT, author, targetNotification);
		return opNot;
	}
}