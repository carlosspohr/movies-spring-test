package br.inf.carlos.bean;

public class MovieDtoRes{

	private String producer;
	private Integer interval;
	private Integer previousWin;
	private Integer followingWin;

	public String getProducer() {
		return producer;
	}

	public void setProducer(String producer) {
		this.producer = producer;
	}

	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	public Integer getPreviousWin() {
		return previousWin;
	}

	public void setPreviousWin(Integer previousWin) {
		this.previousWin = previousWin;
	}

	public Integer getFollowingWin() {
		return followingWin;
	}

	public void setFollowingWin(Integer followingWin) {
		this.followingWin = followingWin;
	}

	@Override
	public String toString() {
		return "MovieDtoRes [producer=" + producer + ", interval=" + interval + ", previousWin=" + previousWin
				+ ", followingWin=" + followingWin + "]";
	}
}
