package com.example.pixaid;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class DonationTypeAdapter extends TypeAdapter<ImpactTrackerController.Donation> {

    @Override
    public void write(JsonWriter out, ImpactTrackerController.Donation donation) throws IOException {
        out.beginObject();
        out.name("date").value(donation.date);
        out.name("amount").value(donation.amount);
        out.endObject();
    }

    @Override
    public ImpactTrackerController.Donation read(JsonReader in) throws IOException {
        ImpactTrackerController.Donation donation = new ImpactTrackerController.Donation();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "date":
                    donation.date = in.nextString();
                    break;
                case "amount":
                    donation.amount = in.nextDouble();
                    break;
            }
        }
        in.endObject();
        return donation;
    }
}