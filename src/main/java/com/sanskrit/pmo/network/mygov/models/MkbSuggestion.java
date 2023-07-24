package com.sanskrit.pmo.network.mygov.models;

import java.util.ArrayList;
import java.util.List;

public class MkbSuggestion {
    final String AppName = "PMO";
    final String CallerId;
    final List<MkbSuggestionContent> Content;
    final String DateTime;
    final String Topic;

    public static class MkbSuggestionContent {
        final String EmailId;
        final String Language;
        final String Message;
        final String MessageTxt;
        final String Name;
        final String NameTxt;
        final String Place = "";
        final String PlaceTxt = "";
        final String SessionId;
        final String State;

        public MkbSuggestionContent(String name, String nametxt, String message, String messageTxt, String language, String sessionId, String email) {
            this.Name = name;
            this.NameTxt = nametxt;
            this.Message = message;
            this.MessageTxt = messageTxt;
            this.Language = language;
            this.SessionId = sessionId;
            this.EmailId = email;
            this.State = "";
        }
    }

    public MkbSuggestion(String callerid, String date, String topic, MkbSuggestionContent content) {
        List<MkbSuggestionContent> contents = new ArrayList();
        contents.add(content);
        this.Content = contents;
        this.CallerId = callerid;
        this.Topic = topic;
        this.DateTime = date;
    }
}
