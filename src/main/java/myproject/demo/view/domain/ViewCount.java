package myproject.demo.view.domain;


import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
public class ViewCount {

    @Id
    private Long novelId;

    @Id
    private Long episodeId;

    @Embedded
    private Count count;

    private ViewCount(Long novelId, Long episodeId, Long count ) {
        this.novelId = novelId;
        this.episodeId = episodeId;
        this.count = Count.create(count);
    }

    public static ViewCount create(Long novelId, Long episodeId, Long count ){
        return new ViewCount(novelId, episodeId, count);
    }

    public void increase(){
        this.count = Count.create(getCount()+1L);
    }

    public void decrease(){
        this.count = Count.create(getCount()-1L);
    }

    public Long getEpisodeId() {
        return this.episodeId;
    }

    public Long getNovelId() {
        return this.novelId;
    }

    public Long getCount(){
        return this.count.getCount();
    }



}
