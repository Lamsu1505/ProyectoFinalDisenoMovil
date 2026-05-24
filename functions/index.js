const { onDocumentCreated } = require("firebase-functions/v2/firestore");
const admin = require("firebase-admin");
admin.initializeApp();

exports.sendPushOnNotification = onDocumentCreated(
    "notifications/{notifId}",
    async (event) => {
        const notification = event.data?.data();
        if (!notification) return null;

        const recipientUid = notification.recipientUid;
        if (!recipientUid) return null;

        const userDoc = await admin.firestore()
            .collection("users")
            .doc(recipientUid)
            .get();

        const fcmToken = userDoc.data()?.fcmToken;
        if (!fcmToken) {
            console.log("Usuario sin fcmToken, omitiendo push");
            return null;
        }

        const message = {
            token: fcmToken,
            notification: {
                title: notification.title,
                body: notification.body,
            },
            data: {
                eventId: notification.eventId ?? "",
                type: notification.type ?? "",
            },
            android: {
                priority: "high",
            },
        };

        try {
            await admin.messaging().send(message);
            console.log("Push enviado a:", recipientUid);
        } catch (error) {
            console.error("Error enviando push:", error);
            if (
                error.code === "messaging/invalid-registration-token" ||
                error.code === "messaging/registration-token-not-registered"
            ) {
                await admin.firestore()
                    .collection("users")
                    .doc(recipientUid)
                    .update({ fcmToken: null });
            }
        }

        return null;
    }
);