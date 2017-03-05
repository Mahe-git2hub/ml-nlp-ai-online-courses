package org.lenskit.mooc.cbf;

import org.lenskit.data.ratings.Rating;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lenskit.mooc.cbf.Helpers.incrementAll;
import static org.lenskit.mooc.cbf.Helpers.multiplyScalarAll;

/**
 * Build a user profile from all positive ratings.
 */
public class WeightedUserProfileBuilder implements UserProfileBuilder {
    /**
     * The tag model, to get item tag vectors.
     */
    private final TFIDFModel model;

    @Inject
    public WeightedUserProfileBuilder(TFIDFModel m) {
        model = m;
    }

    @Override
    public Map<String, Double> makeUserProfile(@Nonnull List<Rating> ratings) {
        // Create a new vector over tags to accumulate the user profile
        Map<String,Double> profile = new HashMap<>();

        double averageRating = 0.0;
        for (Rating r: ratings) {
            averageRating += r.getValue();
        }
        averageRating /= ratings.size();

        // TODO Normalize the user's ratings
        // TODO Build the user's weighted profile
        for (Rating r: ratings) {
            double normRating = r.getValue() - averageRating;
            Map<String, Double> vector = model.getItemVector(r.getItemId());
            Map<String, Double> weightedVector = multiplyScalarAll(vector,normRating);
            long itemId = r.getItemId();
            incrementAll(profile,weightedVector);
        }


        // The profile is accumulated, return it.
        return profile;
    }
}
