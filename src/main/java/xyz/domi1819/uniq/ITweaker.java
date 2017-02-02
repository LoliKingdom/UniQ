package xyz.domi1819.uniq;

public interface ITweaker
{
    public String getName();
    public String getModId();
    public void run(ResourceUnifier unifier) throws Exception;
}
