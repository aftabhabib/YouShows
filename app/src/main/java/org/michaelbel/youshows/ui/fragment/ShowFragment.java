package org.michaelbel.youshows.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.michaelbel.youshows.AndroidExtensions;
import org.michaelbel.material.widget.LayoutHelper;
import org.michaelbel.youshows.Theme;
import org.michaelbel.youshows.YouShows;
import org.michaelbel.youshows.eventbus.Events;
import org.michaelbel.youshows.realm.RealmDb;
import org.michaelbel.youshows.rest.model.Company;
import org.michaelbel.youshows.rest.model.Genre;
import org.michaelbel.youshows.rest.model.Season;
import org.michaelbel.youshows.rest.model.Show;
import org.michaelbel.material.extensions.Extensions;
import org.michaelbel.shows.R;
import org.michaelbel.youshows.ui.ShowActivity;
import org.michaelbel.youshows.ui.view.InfoLayout;
import org.michaelbel.youshows.ui.view.OverviewLayout;
import org.michaelbel.youshows.ui.view.SeasonsLayout;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

/**
 * Date: 19 MAR 2018
 * Time: 12:12 MSK
 *
 * @author Michael Bel
 */

@SuppressLint("CheckResult")
public class ShowFragment extends Fragment {

    private List<Season> list = new ArrayList<>();
    private ShowActivity activity;

    private OverviewLayout overviewLayout;
    private SeasonsLayout seasonsLayout;
    private InfoLayout detailsLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (ShowActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout fragmentLayout = new LinearLayout(activity);
        fragmentLayout.setOrientation(LinearLayout.VERTICAL);
        fragmentLayout.setBackgroundColor(ContextCompat.getColor(activity, Theme.backgroundColor()));

        overviewLayout = new OverviewLayout(activity);
        overviewLayout.setLayoutParams(LayoutHelper.makeLinear(activity, LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));
        fragmentLayout.addView(overviewLayout);

        seasonsLayout = new SeasonsLayout(activity);
        seasonsLayout.recyclerView.setOnItemClickListener((view, position) -> {
            Season season = seasonsLayout.getSeasons().get(position);
            activity.startSeason(season);
        });
        seasonsLayout.setLayoutParams(LayoutHelper.makeLinear(activity, LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 7, 0, 0));
        fragmentLayout.addView(seasonsLayout);

        detailsLayout = new InfoLayout(activity);
        detailsLayout.setLayoutParams(LayoutHelper.makeLinear(activity, LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 7, 0, 0));
        fragmentLayout.addView(detailsLayout);
        return fragmentLayout;
    }

    @Override
    public void onResume() {
        super.onResume();

        ((YouShows) activity.getApplication()).bus().toObservable().subscribe(object -> {
            if (object instanceof Events.UpdateSeasonsView) {
                seasonsLayout.updateAdapter(list);
            }
        });
    }

    public void setName(String name) {
        overviewLayout.setName(name);
    }

    public void setOverview(String overview) {
        overviewLayout.setOverview(TextUtils.isEmpty(overview) ? getString(R.string.NoOverview) : overview);
        overviewLayout.overviewText.setOnLongClickListener(v -> {
            Extensions.copyToClipboard(activity, overview);
            Toast.makeText(activity, R.string.OverviewCopied, Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    public void setOriginalName(String name) {
        detailsLayout.setOriginalName(name);
    }

    public void setStatus(String status) {
        detailsLayout.setStatus(status);
    }

    public void setType(String type) {
        detailsLayout.setType(type);
    }

    public void setDates(String firstAirDate, String lastAirDate) {
        detailsLayout.setDates(
            AndroidExtensions.formatDate(firstAirDate),
            AndroidExtensions.formatDate(lastAirDate)
        );
    }

    public void setHomepage(String homepage) {
        detailsLayout.setHomepage(homepage);
    }

    /*public void setGenres(RealmList<String> genres) {
        detailsLayout.setGenres(AndroidExtensions.formatGenres(genres));
    }*/

    public void setGenres(List<Genre> genres) {
        detailsLayout.setGenres(AndroidExtensions.formatGenres(genres));
    }

    /*public void setOriginalCountries(RealmList<String> countries) {
        detailsLayout.setCountries(AndroidExtensions.formatCountries(countries));
    }*/

    public void setOriginalCountries(List<String> countries) {
        detailsLayout.setCountries(AndroidExtensions.formatCountries(countries));
    }

    /*public void setCompanies(RealmList<String> companies) {
        detailsLayout.setCompanies(AndroidExtensions.formatCompanies(companies));
    }*/

    public void setCompanies(List<Company> companies) {
        detailsLayout.setCompanies(AndroidExtensions.formatCompanies(companies));
    }

    public void setSeasons(Show show) {
        for (Season season : show.seasons) {
            if (season.seasonNumber != 0) {
                list.add(season);
            }
        }

        seasonsLayout.setSeasons(show.showId, list);
        seasonsLayout.setSeasonsTitleCount();
    }

    public void setSeasons(int showId, RealmList<Season> seasons) {
        list.addAll(seasons);

        seasonsLayout.setSeasons(showId, list);
        seasonsLayout.setSeasonsTitleCount();
        RealmDb.updateSeasonsList(showId, list);
    }
}