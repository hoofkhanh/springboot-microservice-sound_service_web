import ArtistIntroduction from "../ArtistIntroduction/ArtistIntroduction";
import HowItWork from "../HowItWork/HowItWork";
import ProductionProsCategory from "../ProductionProsCategory/ProductionProsCategory";
import ReviewContent from "../ReviewContent/ReviewContent";
import SeeWhyContent from "../SeeWhyContent/SeeWhyContent";
import SymbolContent from "../SymbolContent/SymbolContent";
import TermContent from "../TermsContent/TermContent";
import TermsFooter from "../TermsFooter/TermsFooter";

const HomePageContent = () => {
  return ( 
    <>
      <ProductionProsCategory/>
      <ArtistIntroduction />
      <SymbolContent/>
      <HowItWork/>
      <ReviewContent/>
      <TermContent/>
      <SeeWhyContent/>
      <TermsFooter/>
    </>
  );
}
 
export default HomePageContent;